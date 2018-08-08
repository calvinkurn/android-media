package com.tokopedia.topads.sdk.network;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author by errysuprayogi on 3/29/17.
 */

public class JsonResponseConverter implements HttpResponseConverter<JSONObject>{
	
	private JsonResponseConverter(){
		
	}

	@Override
	public JSONObject convertResponse(String httpResponse) {
		try {
			return new JSONObject(httpResponse);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JsonResponseConverter newInstance(){
		return new JsonResponseConverter();
	}

}
