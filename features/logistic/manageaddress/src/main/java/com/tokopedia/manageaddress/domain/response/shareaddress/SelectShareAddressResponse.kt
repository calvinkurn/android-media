package com.tokopedia.manageaddress.domain.response.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.domain.response.ErrorDefaultAddress

data class SelectShareAddressResponse(
    @SerializedName("KeroAddrSelectAddressForShareAddressRequest")
    val data: KeroAddrSelectAddressForShareAddressRequest? = null
) {
    data class KeroAddrSelectAddressForShareAddressRequest(
        @SerializedName("is_success")
        val isSuccess: Boolean = false,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("reply_status")
        val replyStatus: Int? = null,
        @SerializedName("kero_addr_error")
        val error: ErrorDefaultAddress? = null
    )

    val isSuccess: Boolean
        get() = data?.isSuccess == true

    val errorMessage: String
        get() = data?.error?.detail.orEmpty()
}