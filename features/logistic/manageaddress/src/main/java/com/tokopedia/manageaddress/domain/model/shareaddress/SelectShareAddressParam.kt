package com.tokopedia.manageaddress.domain.model.shareaddress

import com.google.gson.annotations.SerializedName

data class SelectShareAddressParam(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("receiver_user_id")
    val receiverUserId: String,
    @SerializedName("sender_address_id")
    val senderAddressId: String,
    @SerializedName("approve")
    val approve: Boolean
) {
    fun toMapParam(): Map<String, Any> = mapOf(PARAM to this)

    companion object {
        private const val PARAM = "param"
    }
}