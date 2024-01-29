package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class DoOptOutResponse(
    @SerializedName("doSellerOutProgram")
    val doSellerOutProgram: DoSellerOutProgram = DoSellerOutProgram()
) {
    data class DoSellerOutProgram(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class ResponseHeader(
            @SerializedName("is_success")
            val isSuccess: Boolean = false
        )
    }
}
