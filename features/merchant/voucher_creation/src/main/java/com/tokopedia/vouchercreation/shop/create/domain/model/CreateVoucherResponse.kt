package com.tokopedia.vouchercreation.shop.create.domain.model

import com.google.gson.annotations.SerializedName

data class CreateVoucherResponse (
        @SerializedName("merchantPromotionCreateMV")
        var merchantPromotionCreateMv: MerchantPromotionCreateMv = MerchantPromotionCreateMv()
)

data class MerchantPromotionCreateMv(
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("process_time")
        val processTime: Float = 0f,
        @SerializedName("data")
        val data: MerchantPromotionCreateMvData = MerchantPromotionCreateMvData()
)

data class MerchantPromotionCreateMvData (
        @SerializedName("redirect_url")
        val redirectUrl: String = "",
        @SerializedName("voucher_id")
        val voucherId: Int = 0,
        @SerializedName("status")
        val status: String = ""
)