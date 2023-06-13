package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DoSellerCampaignCreationRequest(
    @SerializedName("action")
    val action: Int,

    @SerializedName("seller_campaign_type")
    val sellerCampaignType: Int,

    @SuppressLint("Invalid Data Type") @SerializedName("campaign_id")
    val campaignId: Long,

    @SerializedName("campaign_name")
    val campaignName: String,

    @SerializedName("scheduled_start")
    val scheduledStart: String,

    @SerializedName("scheduled_end")
    val scheduledEnd: String,

    @SerializedName("upcoming_time")
    val upcomingTime: String,

    @SerializedName("campaign_relation")
    val campaignRelation: List<Long>,

    @SerializedName("is_campaign_rule_submit")
    val isCampaignRuleSubmit: Boolean,

    @SerializedName("gradient_color")
    val gradientColor: GradientColorInput,

    @SerializedName("show_teaser")
    val showTeaser: Boolean,

    @SerializedName("payment_type")
    val paymentType: Int,

    @SerializedName("package_id")
    val packageId: String
) {

    data class GradientColorInput(
        @SerializedName("first_color") val firstColor: String,
        @SerializedName("second_color") val secondColor: String
    )
}