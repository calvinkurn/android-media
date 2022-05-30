package com.tokopedia.shop.flash_sale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetMerchantCampaignTNCRequest(
    @SuppressLint("Invalid Data Type")
    @SerializedName("campaign_id")
    val campaign_id: Long = 0,
    @SerializedName("is_unique_buyer")
    val is_unique_buyer: Boolean = false,
    @SerializedName("is_campaign_relation")
    val is_campaign_relation: Boolean = false,
    @SerializedName("action_from")
    val action_from: String = "",
    @SerializedName("payment_profile")
    val payment_profile: String = ""
)
