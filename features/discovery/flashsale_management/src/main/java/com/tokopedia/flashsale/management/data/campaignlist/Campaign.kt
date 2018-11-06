package com.tokopedia.flashsale.management.data.campaignlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("campaign_id")
        @Expose
        val campaignId: Long = -1,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("start_date")
        @Expose
        val startDate: String = "",

        @SerializedName("end_date")
        @Expose
        val endDate: String = "",

        @SerializedName("campaign_period")
        @Expose
        val campaignPeriod: String = "",

        @SerializedName("submission_start_date")
        @Expose
        val submissionStartDate: String = "",

        @SerializedName("submission_end_date")
        @Expose
        val submissionEndDate: String = "",

        @SerializedName("review_start_date")
        @Expose
        val reviewStartDate: String = "",

        @SerializedName("review_end_date")
        @Expose
        val reviewnEndDate: String = "",

        @SerializedName("phase_end_date")
        @Expose
        val phaseEndDate: String = "",

        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("campaign_type")
        @Expose
        val campaignType: String = "",

        @SerializedName(value = "cover", alternate = arrayOf("banners"))
        @Expose
        val cover: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",

        @SerializedName("minimum_transaction")
        @Expose
        val minTransaction: String = "",

        /*@SerializedName("banner")
        @Expose
        val banner: String,*/

        @SerializedName("is_joined")
        @Expose
        val isJoined: Boolean = false,

        @SerializedName("dashboard_url")
        @Expose
        val dashboardUrl: String = "",

        @SerializedName("status_info")
        @Expose val statusInfo: StatusInfo = StatusInfo(),

        @SerializedName("criteria")
        @Expose
        val criteria: List<Criteria> = mutableListOf(),

        @SerializedName("tnc")
        @Expose
        val tnc: String = ""
) {
    data class GetCampaignInfo(@SerializedName("data")
                                    @Expose val campaign: Campaign)


    data class Response(@SerializedName("getCampaignInfo")
                        @Expose val result: GetCampaignInfo)
}