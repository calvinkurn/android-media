package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;

public class Errors {
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
