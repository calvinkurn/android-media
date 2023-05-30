package com.tokopedia.chatbot.chatbot2.websocket

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatWebSocketResponse(
    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("code")
    @Expose
    val code: Int = 0,

    @SerializedName("data")
    @Expose
    val jsonElement: JsonElement? = null
) {
    val jsonObject: JsonObject?
        get() = if (jsonElement?.isJsonObject == true) {
            jsonElement as JsonObject
        } else {
            null
        }

    val jsonArray: JsonArray?
        get() = if (jsonElement?.isJsonArray == true) {
            jsonElement as JsonArray
        } else {
            null
        }
}
