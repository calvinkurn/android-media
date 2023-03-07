package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductReserveResponse(
    @SerializedName("doFlashSaleProductReserve" )
    val doFlashSaleProductReserve : DoFlashSaleProductReserve
) {
    data class DoFlashSaleProductReserve (
        @SerializedName("response_header" ) var responseHeader : ResponseHeader      ,
        @SerializedName("product_status"  ) var productStatus  : List<ProductStatus>
    )

    data class ResponseHeader (
        @SerializedName("status"        ) var status       : String       ,
        @SerializedName("error_message" ) var errorMessage : List<String> ,
        @SerializedName("success"       ) var success      : Boolean      ,
        @SerializedName("process_time"  ) var processTime  : Int?         ,
        @SerializedName("error_code"    ) var errorCode    : Int?
    )

    data class ProductStatus (
        @SerializedName("product_id" ) var productId : Long    ,
        @SerializedName("is_success" ) var isSuccess : Boolean ,
        @SerializedName("message"    ) var message   : String
    )
}