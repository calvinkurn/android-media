package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class GoldMerchant(
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false,
        @SerializedName("gold_merchant_logo_url")
        val goldMerchantLogoUrl: String = ""
)