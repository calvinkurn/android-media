package com.tokopedia.manageaddress.domain.response.shareaddress

import com.google.gson.annotations.SerializedName

data class KeroShareAddrToUserResponse(
    @SerializedName("KeroAddrShareAddressToUser")
    var keroAddrSendShareAddressToUser: KeroAddrSendShareAddressData? = null
) {
    val isSuccessShareAddress: Boolean
        get() = (keroAddrSendShareAddressToUser?.numberOfRequest ?: 0) > ZERO_VALUE

    val isSuccessInitialCheck: Boolean
        get() = keroAddrSendShareAddressToUser?.error?.code == ZERO_VALUE

    val errorMessage: String
        get() = keroAddrSendShareAddressToUser?.error?.message.orEmpty()

    companion object {
        private const val ZERO_VALUE = 0
    }
}
