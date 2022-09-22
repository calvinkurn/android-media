package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductRegistrationResponse(
    @SerializedName("doFlashSaleSellerRegistration")
    val doFlashSaleSellerRegistration: DoFlashSaleProductRegistration = DoFlashSaleProductRegistration()
) {
    data class DoFlashSaleProductRegistration(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class ResponseHeader(
            @SerializedName("error_message")
            val errorMessage: List<String> = listOf(),
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}