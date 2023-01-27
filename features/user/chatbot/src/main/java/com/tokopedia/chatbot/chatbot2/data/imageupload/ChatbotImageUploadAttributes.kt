package com.tokopedia.chatbot.chatbot2.data.imageupload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatbotImageUploadAttributes(

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("image_url_thumbnail")
    @Expose
    val thumbnail: String = "",

    @SerializedName("image_url_secure")
    @Expose
    val imageUrlSecure: String = ""
)
