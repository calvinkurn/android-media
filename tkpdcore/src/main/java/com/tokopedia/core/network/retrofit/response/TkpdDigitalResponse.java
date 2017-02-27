package com.tokopedia.core.network.retrofit.response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class TkpdDigitalResponse {

    private JsonObject jsonObject;
    private String fullResponse;
    private String dataResponse;
    private Object dataObject;
    private String message;
    private boolean isError;

    public static TkpdDigitalResponse factory(String strResponse) {
        Gson gson = new Gson();
        TkpdDigitalResponse tkpdDigitalResponse = new TkpdDigitalResponse();
        // gson.fr
        //     if(new JsonObject)
        return tkpdDigitalResponse;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getFullResponse() {
        return fullResponse;
    }

    public void setFullResponse(String fullResponse) {
        this.fullResponse = fullResponse;
    }

    public String getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(String dataResponse) {
        this.dataResponse = dataResponse;
    }

    public Object getDataObject() {
        return dataObject;
    }

    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
