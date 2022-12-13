package com.tokopedia.mvc.data.response

import com.google.gson.annotations.SerializedName

data class MerchantPromotionUpdateStatusMVResponse(
    @SerializedName("merchantPromotionUpdateStatusMV")
    val merchantPromotionUpdateStatusMV: MerchantPromotionUpdateStatusMV = MerchantPromotionUpdateStatusMV()
) {
    data class MerchantPromotionUpdateStatusMV(
        @SerializedName("status")
        val responseStatus: Int = 0,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("process_time")
        val processTime: Float = 0f,
        @SerializedName("data")
        val data: Data = Data()
    ) {
        data class Data(
            @SerializedName("redirect_url")
            val redirectUrl: String = "",
            @SerializedName("voucher_id")
            val voucherId: Long = 0,
            @SerializedName("status")
            val status: String = ""
        )
    }
}
