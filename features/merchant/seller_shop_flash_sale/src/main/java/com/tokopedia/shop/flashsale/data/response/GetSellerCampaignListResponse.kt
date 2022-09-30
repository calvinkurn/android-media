package com.tokopedia.shop.flashsale.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSellerCampaignListResponse(
    @SerializedName("getSellerCampaignList")
    val getSellerCampaignList: GetSellerCampaignList = GetSellerCampaignList()
) {
    data class GetSellerCampaignList(
        @SerializedName("campaign")
        val campaign: List<Campaign> = listOf(),
        @SerializedName("enable_rules")
        val enableRules: Boolean = false,
        @SerializedName("server_time")
        val serverTime: Int = 0,
        @SerializedName("total_campaign")
        val totalCampaign: Int = 0,
        @SerializedName("total_campaign_active")
        val totalCampaignActive: Int = 0,
        @SerializedName("total_campaign_finished")
        val totalCampaignFinished: Int = 0,
        @SerializedName("use_restriction_engine")
        val useRestrictionEngine: Boolean = false
    ) {
        data class Campaign(
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("campaign_name")
            val campaignName: String = "",
            @SerializedName("campaign_relation_data")
            val campaignRelationData: List<CampaignRelationData> = emptyList(),
            @SerializedName("end_date")
            val endDate: Long = 0,
            @SerializedName("gradient_color")
            val gradientColor: GradientColor = GradientColor(),
            @SerializedName("is_campaign_relation")
            val isCampaignRelation: Boolean = false,
            @SerializedName("is_campaign_rule_submit")
            val isCampaignRuleSubmit: Boolean = false,
            @SerializedName("is_cancellable")
            val isCancellable: Boolean = false,
            @SerializedName("is_shareable")
            val isShareable: Boolean = false,
            @SerializedName("is_unique_buyer")
            val isUniqueBuyer: Boolean = false,
            @SerializedName("max_product_submission")
            val maxProductSubmission: Int = 0,
            @SerializedName("notify_me_count")
            val notifyMeCount: Int = 0,
            @SerializedName("payment_type")
            val paymentType: Int = 0,
            @SerializedName("product_summary")
            val productSummary: ProductSummary = ProductSummary(),
            @SerializedName("review_end_date")
            val reviewEndDate: Long = 0,
            @SerializedName("review_start_date")
            val reviewStartDate: Long = 0,
            @SerializedName("start_date")
            val startDate: Long = 0,
            @SerializedName("status_id")
            val statusId: String = "",
            @SerializedName("status_text")
            val statusText: String = "",
            @SerializedName("submission_end_date")
            val submissionEndDate: Long = 0,
            @SerializedName("submission_start_date")
            val submissionStartDate: Long = 0,
            @SerializedName("thematic_participation")
            val thematicParticipation: Boolean = false,
            @SerializedName("thematic_info")
            val thematicInfo: ThematicInfo,
            @SerializedName("use_upcoming_widget")
            val useUpcomingWidget: Boolean = false,
            @SerializedName("package_info")
            val packageInfo: PackageInfo = PackageInfo(),
        ) {
            data class GradientColor(
                @SerializedName("first_color")
                val firstColor: String = "",
                @SerializedName("second_color")
                val secondColor: String = ""
            )

            data class ProductSummary(
                @SerializedName("total_item")
                val totalItem: Int = 0,
                @SerializedName("sold_item")
                val soldItem: Int = 0,
                @SerializedName("reserved_product")
                val reservedProduct: Int = 0,
                @SerializedName("submitted_product")
                val submittedProduct: Int = 0,
                @SerializedName("deleted_product")
                val deletedProduct: Int = 0,
                @SerializedName("visible_product_count")
                val visibleProductCount: Int = 0
            )

            data class CampaignRelationData(
                @SuppressLint("Invalid Data Type")
                @SerializedName("id")
                val id: Long,
                @SerializedName("name")
                val name: String,
            )

            data class ThematicInfo(
                @SuppressLint("Invalid Data Type")
                @SerializedName("thematic_id")
                val id: Long,
                @SerializedName("thematic_sub_id")
                val subId: Long,
                @SerializedName("thematic_name")
                val name: String,
                @SerializedName("status")
                val status: Long,
                @SerializedName("status_str")
                val statusString: String,
            )
            data class PackageInfo(
                @SerializedName("package_id")
                val packageId: Long = 0,
                @SerializedName("package_name")
                val packageName: String = "",
            )
        }
    }
}