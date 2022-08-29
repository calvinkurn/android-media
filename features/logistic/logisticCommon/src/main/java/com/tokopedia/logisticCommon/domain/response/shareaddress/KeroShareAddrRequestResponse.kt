package com.tokopedia.logisticCommon.domain.response.shareaddress

import com.google.gson.annotations.SerializedName

data class KeroShareAddrRequestResponse(
    @SerializedName("KeroAddrSendShareAddressRequest")
    var keroAddrSendShareAddressRequest: KeroAddrSendShareAddressData? = null
) {
    val isSuccess: Boolean
        get() = (keroAddrSendShareAddressRequest?.numberOfRequest ?: 0) > ZERO_VALUE

    val errorMessage: String
        get() = keroAddrSendShareAddressRequest?.errorMessage ?: ""

    companion object {
        private const val ZERO_VALUE = 0
    }
}