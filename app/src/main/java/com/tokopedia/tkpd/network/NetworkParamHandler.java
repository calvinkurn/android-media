package com.tokopedia.tkpd.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ricoharisin on 2/13/15.
 */
@Deprecated
public class NetworkParamHandler {

    private JSONObject JsonPutter = new JSONObject();

    public NetworkParamHandler() {

    }

    public void AddParam(String key, String value) {
        try {
            JsonPutter.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddParam(String key, Boolean value) {
        try {
            JsonPutter.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddParam(String key, Integer value) {
        try {
            JsonPutter.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJSONObject() {
        return JsonPutter;
    }

}
