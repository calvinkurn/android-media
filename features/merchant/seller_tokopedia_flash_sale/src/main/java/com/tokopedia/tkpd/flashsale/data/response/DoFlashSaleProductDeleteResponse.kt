package com.tokopedia.tkpd.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductDeleteResponse(
    @SerializedName("doFlashSaleProductDelete")
    val doFlashSaleProductDelete: DoFlashSaleProductDelete = DoFlashSaleProductDelete()
) {
    data class DoFlashSaleProductDelete(
        @SerializedName("product_status")
        val productStatus: List<ProductStatus> = listOf(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class ProductStatus(
            @SerializedName("is_success")
            val isSuccess: Boolean = false,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("product_id")
            val productId: Long = 0
        )

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
