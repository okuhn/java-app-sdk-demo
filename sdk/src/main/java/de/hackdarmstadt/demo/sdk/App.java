package de.hackdarmstadt.demo.sdk;

import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.RawMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

/**
 * Minimal class to test the MQTT support for The Things Network Java SDK library.
 */
public class App {

	public static final String APPLICATION_ID = "lopy-noise-app";
	public static final String APPLICATION_KEY = "ttn-account-v2.dIwg3SFt_FLt78TGi-DrrlgNCxI8FjxUdpMm5BqaA0g";
	public static final String REGION = "eu";

	public static void main( String[] args ) {
		Client client = createClient();
		registerCallbackHandlers(client);
		startClient(client);
		
		System.out.println("Waiting for events ...");
	}
	
	/**
	 * Callback handler for connect events.
	 */
	private static void handleConnect(Connection connection) {
		System.out.println("Connected: " + connection.toString());
	}
	
	/**
	 * Callback handler for activation events.
	 */
	private static void handleActivation(String deviceId, ActivationMessage data) {
		System.out.println("Activation: " + deviceId + ", data: " + data.getDevAddr());
	}
	
	/**
	 * Callback handler for device events.
	 */
	private static void handleDevice(String deviceId, String event, RawMessage data) {
		System.out.println("Device: " + deviceId + ", event: " + event + ", data: " + data.toString());
	}
	
	/**
	 * Callback handler for message events.
	 */
	private static void handleMessage(String deviceId, DataMessage data) {
		System.out.println("Message: " + deviceId + " " + ((UplinkMessage) data).getCounter());		
	}
	
	/**
	 * Callback handler for error events.
	 */
	private static void handleEror(Throwable error) {
		System.err.println("Exception: " + error.getMessage());		
	}

	/**
	 * Creates and returns a The Things Network MQTT client.
	 */
	private static Client createClient() {
		System.out.println("Create MQTT client");

		Client client = null;

		try {
			client = new Client(REGION, APPLICATION_ID, APPLICATION_KEY);
		}
		catch (Exception e) {
			System.err.println("Failed to create MQTT client:" + e.getMessage());
		}

		return client;
	}
	
	/**
	 * Registers some callback handlers for messages etc. with the MQTT client.
	 */
	private static void registerCallbackHandlers(Client client) {
		System.out.println("Register callback handlers");

		client.onConnected((Connection cnx) 
				-> handleConnect(cnx));

		client.onActivation((String devId, ActivationMessage data) 
				-> handleActivation(devId, data));
		
		client.onDevice((String devId, String event, RawMessage data) 
				-> handleDevice(devId, event, data));
		
		client.onMessage((String devId, DataMessage data) 
				-> handleMessage(devId, data));
		
		client.onError((Throwable error) 
				-> handleEror(error));
	}

	/**
	 * Starts the MQTT client.
	 */
	private static void startClient(Client client) {
		try {
			client.start();
		} 
		catch (Exception e) {
			System.err.println("Failed to start MQTT client:" + e.getMessage());
		}

		System.out.println("MQTT client successfully started");
	}
}
