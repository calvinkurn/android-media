package com.tokopedia.logisticCommon.domain.request

import com.google.gson.annotations.SerializedName

data class SendShareAddressRequestParam(
    @SerializedName("source")
    var source: String = "",
    @SerializedName("sender_phone_number_or_email")
    var senderPhoneNumberOrEmail: String
) {
    fun toMapParam() : Map<String, Any> = mapOf(PARAM to this)

    companion object {
        private const val PARAM = "param"
    }
}
