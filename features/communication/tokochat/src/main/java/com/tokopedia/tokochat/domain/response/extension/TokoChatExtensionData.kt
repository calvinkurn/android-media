package com.tokopedia.tokochat.domain.response.extension

import com.google.gson.annotations.SerializedName

data class TokoChatExtensionData (
    @SerializedName("id")
    var id: String? = "",

    @SerializedName("message_id")
    var messageId: String? = "",

    @SerializedName("widget_id")
    var widgetId: String? = "",

    @SerializedName("version")
    var version: Long? = 0,

    @SerializedName("message_text")
    var messageText: String? = "",

    @SerializedName("payload")
    var payload: String? = "",

    @SerializedName("tracking_id")
    var trackingId: String? = ""
) {
    var extensionPayload: TokoChatExtensionPayload? = null
}
