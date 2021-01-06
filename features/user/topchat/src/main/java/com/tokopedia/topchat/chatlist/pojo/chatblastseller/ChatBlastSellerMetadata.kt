package com.tokopedia.topchat.chatlist.pojo.chatblastseller


import com.google.gson.annotations.SerializedName

data class ChatBlastSellerMetadata(
        @SerializedName("url")
        val url: String = "",
        @SerializedName("status")
        val status: Int = -1
) {
    fun isWhiteListed(): Boolean {
        return status == 1
    }
}