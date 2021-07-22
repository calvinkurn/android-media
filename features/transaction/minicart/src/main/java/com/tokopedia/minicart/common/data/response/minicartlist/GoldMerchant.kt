package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class GoldMerchant(
        @SerializedName("gold_merchant_logo_url")
        val goldMerchantLogoUrl: String = "",
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false
)