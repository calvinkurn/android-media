package com.tokopedia.manageaddress.domain.model.shareaddress

import com.google.gson.annotations.SerializedName

data class SenderShareAddressParam(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("sender_user_ids")
    val senderUserIds: List<String>,
) {
    fun toMapParam() : Map<String, Any> = mapOf(PARAM to this)

    companion object {
        private const val PARAM = "param"
    }
}