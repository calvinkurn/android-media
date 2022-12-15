package com.tokopedia.tkpd.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductSubmissionResponse(
    @SerializedName("doFlashSaleProductSubmission")
    val doFlashSaleProductSubmission: DoFlashSaleProductSubmission = DoFlashSaleProductSubmission()
) {
    data class DoFlashSaleProductSubmission(
        @SerializedName("product_status")
        val productStatus: List<ProductStatu> = listOf(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class ProductStatu(
            @SerializedName("is_success")
            val isSuccess: Boolean = false,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("product_id")
            val productId: Long = 0,
            @SerializedName("warehouses")
            val warehouses: List<Warehouse> = listOf()
        ) {
            data class Warehouse(
                @SerializedName("is_success")
                val isSuccess: Boolean = false,
                @SerializedName("message")
                val message: String = "",
                @SerializedName("warehouse_id")
                val warehouseId: String = ""
            )
        }

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
