package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayStoreData(
        @SerializedName("playstore_status")
        @Expose
        val status: String = "",
        @SerializedName("playstore_message")
        @Expose
        val message: String = "",
        @SerializedName("playstore_redirect_url")
        @Expose
        val redirectUrl: String = ""
) {

    fun isBanned(): Boolean {
        return status == PLAY_STORE_BANNED || status == PLAY_STORE_SUSPICIOUS
    }

    companion object {
        const val PLAY_STORE_NORMAL = "NORMAL"
        // PLAY_STORE_Suspicious is enum for playstore which product is suspicious
        const val PLAY_STORE_SUSPICIOUS = "SUSPICIOUS"
        // PLAY_STORE_Banned is enum for playstore which product is Banned
        const val PLAY_STORE_BANNED = "BANNED"
        // PLAY_STORE_Whitelist is enum for playstore which product is Whitelist
        const val PLAY_STORE_WHITELISTED = "WHITELISTED"
    }
}