package com.tokopedia.notifications.data.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("cmAddToken")
    JsonObject cmAddToken;

    public JsonObject getCmAddToken() {
        return cmAddToken;
    }

    public void setCmAddToken(JsonObject cmAddToken) {
        this.cmAddToken = cmAddToken;
    }
}
