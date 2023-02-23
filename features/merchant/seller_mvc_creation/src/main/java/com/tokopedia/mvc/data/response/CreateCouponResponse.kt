package com.tokopedia.mvc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateCouponResponse(
    @SerializedName("merchantPromotionCreateMV")
    @Expose
    val merchantPromotionCreateMV: MerchantPromotionCreateMV = MerchantPromotionCreateMV()
) {
    data class Data(
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("voucher_id")
        @Expose
        val voucherId: String = ""
    )

    data class MerchantPromotionCreateMV(
        @SerializedName("data")
        @Expose
        val data: Data = Data(),
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("process_time")
        @Expose
        val processTime: Double = 0.0,
        @SerializedName("status")
        @Expose
        val status: Int = 0
    )
}
