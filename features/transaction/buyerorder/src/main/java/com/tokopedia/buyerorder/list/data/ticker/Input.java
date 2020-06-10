package com.tokopedia.buyerorder.list.data.ticker;

import com.google.gson.annotations.SerializedName;

public class Input {

    @SerializedName("request_by")
    private String request_by;
    @SerializedName("client")
    private String client;
    @SerializedName("user_id")
    private String user_id;

    public String getRequest_by() {
        return request_by;
    }

    public void setRequest_by(String request_by) {
        this.request_by = request_by;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}