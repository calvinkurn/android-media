package com.tokopedia.logisticCommon.domain.response.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.domain.response.ErrorDefaultAddress

data class KeroAddrSendShareAddressData(
    @SerializedName("number_of_request")
    var numberOfRequest: Int? = null,
    @SerializedName("kero_addr_error")
    var error: ErrorDefaultAddress? = null,
) {
    val errorMessage: String
        get() = when(error?.code) {
            INVALID_PHONE_NUMBER_ERROR_CODE, USER_NOT_FOUND_ERROR_CODE -> NO_ACCOUNT_ERROR_MESSAGE
            REQUEST_ALREADY_EXIST_ERROR_CODE -> ALREADY_USED_ERROR_MESSAGE
            else -> error?.detail ?: ""
        }

    companion object {
        private const val INVALID_PHONE_NUMBER_ERROR_CODE = 6
        private const val USER_NOT_FOUND_ERROR_CODE = 10
        private const val REQUEST_ALREADY_EXIST_ERROR_CODE = 15

        private const val NO_ACCOUNT_ERROR_MESSAGE = "Nomor ini tidak memiliki akun Tokopedia"
        private const val ALREADY_USED_ERROR_MESSAGE = "Nomor ini sudah pernah digunakan"
    }
}