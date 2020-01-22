package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

data class GoldMerchant(
    @SerializedName("is_gold")
    @Expose
    val isGold: Int = 0,
    @SerializedName("is_gold_badge")
    @Expose
    val isGoldBadge: Boolean = false,
    @SerializedName("gold_merchant_logo_url")
    @Expose
    val goldMerchantLogoUrl: String = ""
)