package com.tokopedia.logisticCommon.domain.response

import com.google.gson.annotations.SerializedName

data class ShareAddressResponse(
    @SerializedName("shareAddressResponse")
    var shareAddressResponse: ShareAddressResponse = ShareAddressResponse()
) {
    data class ShareAddressResponse (
        @SerializedName("isSuccess")
        var isSuccess: Boolean = false,
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("errorMessage")
        var error: String = ""
    )
}