package com.tokopedia.shopdiscount.common.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DoSlashPriceProductReservationResponse(
    @SerializedName("DoSlashPriceProductReservation")
    @Expose
    var doSlashPriceProductReservation: DoSlashPriceProductReservation = DoSlashPriceProductReservation()
) {
    data class DoSlashPriceProductReservation(
        @SerializedName("response_header")
        @Expose
        var responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("failed_products")
        @Expose
        var listFailedProduct: List<FailedProduct> = listOf()
    ) {
        data class FailedProduct(
            @SerializedName("product_id")
            @Expose
            var productId: String = "",
            @SerializedName("name")
            @Expose
            var productName: String = "",
            @SerializedName("url")
            @Expose
            var url: String = "",
            @SerializedName("message")
            @Expose
            var message: String = "",
            @SerializedName("error_code")
            @Expose
            var errorCode: Int = 0
        )
    }
}