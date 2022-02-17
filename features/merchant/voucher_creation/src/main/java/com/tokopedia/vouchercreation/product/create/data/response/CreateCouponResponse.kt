package com.tokopedia.vouchercreation.product.create.data.response


import com.google.gson.annotations.SerializedName

data class CreateCouponResponse(
    @SerializedName("merchantPromotionCreateMV")
    val merchantPromotionCreateMV: MerchantPromotionCreateMV = MerchantPromotionCreateMV()
)

data class Data(
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("voucher_id")
    val voucherId: Int = 0
)
data class MerchantPromotionCreateMV(
    @SerializedName("data")
    val data: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("process_time")
    val processTime: Double = 0.0,
    @SerializedName("status")
    val status: Int = 0
)