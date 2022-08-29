package com.tokopedia.logisticCommon.domain.request

import com.google.gson.annotations.SerializedName

data class ShareAddressToUserParam(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("sender_address_id")
    val senderAddressId: String,
    @SerializedName("receiver_phone_number_or_email")
    val receiverPhoneNumberOrEmail: String,
    @SerializedName("initial_check")
    val initialCheck: Boolean
) {
    fun toMapParam() : Map<String, Any> = mapOf(PARAM to this)

    companion object {
        private const val PARAM = "param"
    }
}