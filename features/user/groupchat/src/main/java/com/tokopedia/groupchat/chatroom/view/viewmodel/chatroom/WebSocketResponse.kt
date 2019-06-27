package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WebSocketResponse {

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("data")
    @Expose
    var data: JsonObject? = null
}
