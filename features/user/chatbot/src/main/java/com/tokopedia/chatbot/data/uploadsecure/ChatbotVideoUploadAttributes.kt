package com.tokopedia.chatbot.data.uploadsecure

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatbotVideoUploadAttributes (
    @SerializedName("video_url")
    @Expose
    val videoUrl: String = "",
    @SerializedName("thumbnail")
    @Expose
    val thumbNail: String = "",
)