package com.tokopedia.core.manage.people.bank.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public class BcaOneClickData {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("message")
    private String message;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
