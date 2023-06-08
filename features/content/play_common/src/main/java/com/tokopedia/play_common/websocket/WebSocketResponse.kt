package com.tokopedia.play_common.websocket

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 06/12/18
 */
data class WebSocketResponse(
    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("code")
    @Expose
    val code: Int = 0,

    @SerializedName("data")
    @Expose
    val jsonElement: JsonElement? = null,

    @SerializedName("tracking")
    @Expose
    val tracking: TrackingField = TrackingField()
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

    data class TrackingField(
        @SerializedName("id")
        @Expose
        val id: String = ""
    )
}

val WebSocketResponse.isTrackingIdAvailable: Boolean
    get() = this.tracking.id.isNotBlank()
