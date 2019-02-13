package com.tokopedia.core.network.exception.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Vishal on 4/20/17.
 * <p>
 * This class handles 422 http error codes
 */

@Deprecated
public class UnProcessableHttpException extends IOException {
    private static final String DEFAULT_MESSAGE = "Sorry, Please try again later";

    private String title;
    private String code;

    public UnProcessableHttpException() {
        super(DEFAULT_MESSAGE);
        title = DEFAULT_MESSAGE;
    }

    public UnProcessableHttpException(String message, Throwable cause) {
        super(message, cause);
        title = DEFAULT_MESSAGE;
    }

    public UnProcessableHttpException(String errorMessage) {
        super(DEFAULT_MESSAGE);
        JSONObject dataJsonObject = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(errorMessage);
            dataJsonObject = jsonObject.optJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        title = "";

        if (dataJsonObject != null) {
            title = dataJsonObject.optString("message");
            code = dataJsonObject.optString("code");
        }

        //parse message_error array and combined all strings
        try {
            if (title != null && title.length() > 0) {
                return;
            }

            String JSON_ERROR_KEY = "message_error";
            if (jsonObject != null && jsonObject.has(JSON_ERROR_KEY)) {
                JSONArray messageErrorArray = jsonObject.optJSONArray(JSON_ERROR_KEY);
                if (messageErrorArray != null) {
                    for (int index = 0; index < messageErrorArray.length(); index++) {
                        if (index > 0) {
                            title += ", ";
                        }

                        title = title + messageErrorArray.getString(index);
                    }
                } else {
                    title = jsonObject.getString(JSON_ERROR_KEY);
                }
            }
        } catch (Exception ex) {
            title = DEFAULT_MESSAGE;
        }
    }

    @Override
    public String getMessage() {
        return title;
    }

    public String getCode() {
        return code;
    }
}