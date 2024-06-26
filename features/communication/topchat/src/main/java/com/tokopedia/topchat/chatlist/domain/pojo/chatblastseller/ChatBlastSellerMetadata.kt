package com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller


import com.google.gson.annotations.SerializedName

data class ChatBlastSellerMetadata(
        @SerializedName("urlBroadcast")
        var urlBroadcast: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("status")
        val status: Int = -1
) {
    fun isWhiteListed(): Boolean {
        return status == 1
    }
}