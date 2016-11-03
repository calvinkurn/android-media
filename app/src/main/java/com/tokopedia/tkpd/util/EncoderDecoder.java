package com.tokopedia.tkpd.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class EncoderDecoder {
	
	public String Encrypt(String text, String initialVector) {
		byte[] raw = new byte[] {'g','g','g','g','t','t','t','t','t','u','j','k','r','r','r','r'};   
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");   
    	//String initialVector = "abcdefgh";
    	String encode_result = null;
    	IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
    	System.out.println("result: ");
    	try {
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
	    	byte[] encryptedData = cipher.doFinal(text.getBytes());     
	    	encode_result = Base64.encodeToString(encryptedData, 0);
	    	encode_result.replace("\n", "");
    	}
    	catch (Exception e) {
    		e.printStackTrace();  		
    	}
		return encode_result;
	}
	
	public static String Encrypt(String text, String initialVector, byte[] raw) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");   
    	//String initialVector = "abcdefgh";
    	String encode_result = null;
    	IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
    	System.out.println("result: ");
    	try {
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    	cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
	    	byte[] encryptedData = cipher.doFinal(text.getBytes());     
	    	encode_result = Base64.encodeToString(encryptedData, 0);
	    	encode_result.replace("\n", "");
    	}
    	catch (Exception e) {
    		e.printStackTrace();  		
    	}
		return encode_result;
	}
	
	public static String Decrypt (String text, String initialVector, byte[] raw) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");   
    	//String initialVector = "abcdefgh";
    	String decode_result = null;
    	IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
    	System.out.println("result: ");
    	try {
    		byte[] data = Base64.decode(text, Base64.DEFAULT);
	    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    	cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivs);
	    	byte[] decryptedData = cipher.doFinal(data);     
	    	decode_result = new String(decryptedData);
	    	//decode_result = Base64.encodeToString(encryptedData, 0);
	    	//decode_result.replace("\n", "");
    	}
    	catch (Exception e) {
    		e.printStackTrace();  		
    	}
		return decode_result;
	}

	public static String Decrypt (String text, String initialVector) {
		byte[] raw = new byte[] {'g','g','g','g','t','t','t','t','t','u','j','k','r','r','r','r'};
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		//String initialVector = "abcdefgh";
		String decode_result = null;
		IvParameterSpec ivs = new IvParameterSpec(initialVector.getBytes());
		System.out.println("result: ");
		try {
			byte[] data = Base64.decode(text, Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivs);
			byte[] decryptedData = cipher.doFinal(data);
			decode_result = new String(decryptedData);
			//decode_result = Base64.encodeToString(encryptedData, 0);
			//decode_result.replace("\n", "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return decode_result;
	}

}
