package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmKuponUpdateMVResponse(
    @Expose
    @SerializedName("merchantPromotionUpdateMV")
    val merchantPromotionCreateMV: MerchantPromotionUpdateMV? = null
)

data class MerchantPromotionUpdateMV(

    @SerializedName("data")
    val data: DataInnerUpdate? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("process_time")
    val processTime: Double? = null,

    @SerializedName("status")
    val status: Int? = null
)

data class DataInnerUpdate(

    @SerializedName("voucher_id")
    val voucherId: String? = null,

    @SerializedName("redirect_url")
    val redirectUrl: String? = null,

    @SerializedName("status")
    val status: String? = null
)