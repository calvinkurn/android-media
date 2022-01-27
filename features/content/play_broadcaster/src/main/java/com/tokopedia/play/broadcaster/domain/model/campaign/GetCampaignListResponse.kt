package com.tokopedia.play.broadcaster.domain.model.campaign

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 25/01/22.
 */
data class GetCampaignListResponse(
    @SerializedName("getSellerCampaignList")
    val getSellerCampaignList: GetSellerCampaignList = GetSellerCampaignList()
) {

    data class GetSellerCampaignList(
        @SerializedName("campaign")
        val campaigns: List<Campaign> = listOf(),

        @SerializedName("total_campaign")
        val totalCampaign: Int = 0,

        @SerializedName("total_campaign_active")
        val totalCampaignActive: Int = 0,

        @SerializedName("total_campaign_finished")
        val totalCampaignFinished: Int = 0,
    )

    data class Campaign(
        @SerializedName("campaign_id")
        val campaignId: String = "",

        @SerializedName("campaign_name")
        val campaignName: String = "",

        @SerializedName("campaign_type_id")
        val campaignTypeId: String = "",

        @SerializedName("campaign_type_name")
        val campaignTypeName: String = "",

        @SerializedName("status_id")
        val statusId: String = "",

        @SerializedName("status_text")
        val statusText: String = "",

        @SerializedName("status_detail")
        val statusDetail: String = "",

        @SerializedName("start_date")
        val startDate: Long = 0,

        @SerializedName("end_date")
        val endDate: Long = 0,

        @SerializedName("submission_start_date")
        val submissionStartDate: Long = 0,

        @SerializedName("submission_end_date")
        val submissionEndDate: Long = 0,

        @SerializedName("review_start_date")
        val reviewStartDate: Long = 0,

        @SerializedName("review_end_date")
        val reviewEndDate: Long = 0,

        @SerializedName("product_summary")
        val productSummary: ProductSummary = ProductSummary(),

        @SerializedName("max_product_submission")
        val maxProductSubmission: Int = 0,

        @SerializedName("etalase_prefix")
        val etalasePrefix: String = "",

        @SerializedName("redirect_url")
        val redirectUrl: String = "",

        @SerializedName("redirect_url_app")
        val redirectUrlApp: String = "",

        @SerializedName("campaign_banner")
        val campaignBanner: List<SellerCampaignBanner> = listOf(),

        @SerializedName("finished_widget_time")
        val finishedWidgetTime: Long = 0,

        @SerializedName("finished_widget_time_in_mins")
        val finishedWidgetTimeInMins: Long = 0,

        @SerializedName("campaign_relation_data")
        val campaignRelationData: List<CampaignRelationData> = listOf(),

        @SerializedName("is_unique_buyer")
        val isUniqueBuyer: Boolean = false,

        @SerializedName("is_campaign_relation")
        val isCampaignRelation: Boolean = false,

        @SerializedName("is_campaign_rule_submit")
        val isCampaignRuleSubmit: Boolean = false,

        @SerializedName("is_shareable")
        val isShareable: Boolean = false,

        @SerializedName("bitmask_is_set")
        val bitmaskIsSet: Boolean = false,

        @SerializedName("gradient_color")
        val gradientColor: GradientColor = GradientColor(),

        @SerializedName("notify_me_count")
        val notifyMeCount: Int = 0,

        @SerializedName("use_upcoming_widget")
        val useUpcomingWidget: Boolean = false,

        @SerializedName("payment_type")
        val paymentPype: Int = 0,

        @SerializedName("cover_img")
        val coverImg: String = "",
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
        val visibleProduct_count: Int = 0,
    )

    data class SellerCampaignBanner(

        @SerializedName("banner_id")
        val bannerId: String = "",

        @SerializedName("file_path")
        val filePath: String = "",

        @SerializedName("banner_type")
        val bannerType: String = "",
    )

    data class CampaignRelationData(

        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",
    )

    data class GradientColor(

        @SerializedName("first_color")
        val firstColor: String = "",

        @SerializedName("second_color")
        val secondColor: String = "",
    )
}