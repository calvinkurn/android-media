package com.tokopedia.chatbot.chatbot2.data.dynamicAttachment

import com.google.gson.annotations.SerializedName

data class MediaButtonAttribute(
    @SerializedName("is_media_button_enabled")
    val isMediaButtonEnabled: Boolean = false,
    @SerializedName("attributes")
    val buttons: MediaButtonItem? = null
) {
    data class MediaButtonItem(
        @SerializedName("is_upload_image_enabled")
        val isUploadImageEnabled: Boolean? = false,
        @SerializedName("is_upload_video_enabled")
        val isUploadVideoEnabled: Boolean? = false
    )
}
