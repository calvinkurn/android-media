package com.tokopedia.shop_nib.data.response


import com.google.gson.annotations.SerializedName

data class SellerSubmitNIBStatusResponse(
    @SerializedName("sellerSubmitNIBStatus")
    val sellerSubmitNIBStatus: SellerSubmitNIBStatus = SellerSubmitNIBStatus()
) {
    data class SellerSubmitNIBStatus(
        @SerializedName("error")
        val error: Error? = Error(),
        @SerializedName("result")
        val result: Result? = Result()
    ) {
        data class Error(
            @SerializedName("message")
            val message: String? = ""
        )

        data class Result(
            @SerializedName("status")
            val status: Int? = 0,
            @SerializedName("updateTime")
            val updateTime: String? = ""
        )
    }
}
