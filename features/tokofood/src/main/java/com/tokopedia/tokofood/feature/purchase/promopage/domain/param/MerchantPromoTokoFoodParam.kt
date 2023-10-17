package com.tokopedia.tokofood.feature.purchase.promopage.domain.param

import com.google.gson.annotations.SerializedName

data class MerchantPromoTokoFoodParam(
    @SerializedName("additional_attributes")
    val additionalAttributes: String = "",
    @SerializedName("source")
    val source: String = "",
    @SerializedName("merchant_id")
    val merchantId: String = ""
)
