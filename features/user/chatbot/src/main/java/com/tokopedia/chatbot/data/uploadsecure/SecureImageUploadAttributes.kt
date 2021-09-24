package com.tokopedia.chatbot.data.uploadsecure


import com.google.gson.annotations.SerializedName

data class SecureImageUploadAttributes(
    @SerializedName("image_url_secure")
    val imageUrlSecure: String
)