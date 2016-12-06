package com.tokopedia.core.util;

import com.tkpd.library.utils.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class JSONHandler {
	
	private RestClient rclient;
	private JSONObject json;
	private EncoderDecoder encoder;
	private String IDunique = null;
	private int status = 0;
	private String response;
	
	public JSONHandler (String url) {
		rclient = new RestClient(url , 0);
		json = new JSONObject();
		encoder = new EncoderDecoder();
	}
	
	public void AddJSON (String name, String value) throws JSONException {
	    json.put(name,value);
	}
	    
	public String GetJSONString () throws JSONException {
    	String json_string = json.toString();
    	return json_string;
    }
    
    public int CompileJSON() throws Exception {
    	IDunique = UUID.randomUUID().toString();
    	String id = IDunique.replaceAll("-", "");
    	String iv = id.substring(0, 16);
    	System.out.println("sending to ws:"+json.toString());
    	String encrypt_data = encoder.Encrypt(json.toString(),iv);
    	rclient.AddParam("sc",encrypt_data);
    	rclient.AddParam("iv",iv);
    	//System.out.println("SC: "+encrypt_data);
    	//System.out.println("SC: "+encrypt_data);
    	status = rclient.Execute("POST");
    	return status;
    }
    
    public String getResponse() {
    	response = rclient.getResponse();
    	return response;
    }
    
}
