package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductSubmitAcknowledgeResponse(
    @SerializedName("doFlashSaleProductSubmitAcknowledge")
    val doFlashSaleProductSubmitAcknowledge: DoFlashSaleProductSubmitAcknowledge = DoFlashSaleProductSubmitAcknowledge()
) {
    data class DoFlashSaleProductSubmitAcknowledge(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("is_success")
        val isSuccess: Boolean = false
    ) {
        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: Int = 0,
            @SerializedName("error_message")
            val errorMessage: List<String> = listOf(),
            @SerializedName("process_time")
            val processTime: Int = 0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}
