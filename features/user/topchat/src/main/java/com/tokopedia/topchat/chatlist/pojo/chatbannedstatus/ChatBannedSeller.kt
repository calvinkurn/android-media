package com.tokopedia.topchat.chatlist.pojo.chatbannedstatus


import com.google.gson.annotations.SerializedName

data class ChatBannedSeller(
    @SerializedName("status")
    val status: Int = 1
) {
    fun isBanned(): Boolean {
        return status == STATUS_BANNED
    }

    companion object {
        const val STATUS_BANNED = 0
    }
}