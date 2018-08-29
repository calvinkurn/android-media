package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Status {

    private int errorCode;
    private String message;

    private static final String KEY_ERROR_CODE = "error_code";
    private static final String KEY_MESSAGE = "message";

    public Status(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ERROR_CODE)) {
            setErrorCode(object.getInt(KEY_ERROR_CODE));
        }
        if(!object.isNull(KEY_MESSAGE)) {
            setMessage(object.getString(KEY_MESSAGE));
        }
    }

    public Status(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
