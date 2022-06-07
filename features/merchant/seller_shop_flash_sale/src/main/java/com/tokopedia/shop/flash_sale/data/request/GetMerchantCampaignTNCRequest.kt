package com.tokopedia.shop.flash_sale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetMerchantCampaignTNCRequest(
    @SuppressLint("Invalid Data Type")
    @SerializedName("campaign_id")
    val campaignId: Long = 0,
    @SerializedName("is_unique_buyer")
    val isUniqueBuyer: Boolean = false,
    @SerializedName("is_campaign_relation")
    val isCampaignRelation: Boolean = false,
    @SerializedName("action_from")
    val actionFrom: String = "",
    @SerializedName("payment_profile")
    val paymentProfile: String = ""
)
