
package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebSocketResponse {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("data")
    @Expose
    private JsonObject jsonObject;

    public String getType() {
        return type;
    }

    public void setType(String code) {
        this.type = code;
    }

    public JsonObject getData() {
        return jsonObject;
    }

    public void setData(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
