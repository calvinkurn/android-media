package com.tokopedia.websocket

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 06/12/18
 */
data class WebSocketResponse(
        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("code")
        @Expose
        var code: Int = 0,

        @SerializedName("data")
        @Expose
        var jsonObject: JsonObject? = null)
