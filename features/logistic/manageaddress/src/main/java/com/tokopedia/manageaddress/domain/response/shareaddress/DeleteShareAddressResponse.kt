package com.tokopedia.manageaddress.domain.response.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.domain.response.ErrorDefaultAddress

data class DeleteShareAddressResponse(
    @SerializedName("KeroAddrDeleteSharedAddress")
    val data: KeroAddrDeleteSharedAddress? = null
) {
    data class KeroAddrDeleteSharedAddress(
        @SerializedName("is_success")
        val isSuccess: Boolean = false,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("number_address_deleted")
        val numberAddressSaved: Int? = null,
        @SerializedName("kero_addr_error")
        val error: ErrorDefaultAddress? = null
    )

    val isSuccess: Boolean
        get() = data?.isSuccess == true

    val errorMessage: String
        get() = data?.error?.detail.orEmpty()
}