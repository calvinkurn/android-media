package com.tokopedia.developer_options.api;

import com.google.gson.annotations.SerializedName;

public class FeedbackResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("self")
    private String self;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }
}
