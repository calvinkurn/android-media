package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmKuponUpdateMVResponse(
    @Expose
    @SerializedName("merchantPromotionUpdateMV")
    val merchantPromotionCreateMV: MerchantPromotionCreateMultipleMV? = null
)
