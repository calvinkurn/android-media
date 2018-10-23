package com.tokopedia.core.gcm.domain;

/**
 * Created by alvarisi on 2/22/17.
 */

public class PushNotification {
    private String category;
    private String response;

    public PushNotification() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
