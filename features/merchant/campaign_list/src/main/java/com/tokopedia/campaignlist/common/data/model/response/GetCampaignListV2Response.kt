package com.tokopedia.campaignlist.common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetCampaignListV2Response(
        @SerializedName("getCampaignListV2")
        @Expose val getCampaignListV2: GetCampaignListV2 = GetCampaignListV2()
)

data class GetCampaignListV2(
        @SerializedName("data")
        @Expose val campaignList: List<CampaignListV2> = listOf(),
        @SerializedName("total_campaign")
        @Expose val totalCampaign: Int = 0,
        @SerializedName("total_campaign_active")
        @Expose val totalCampaignActive: Int = 0,
        @SerializedName("enable_rules")
        @Expose val enableRules: Boolean = false,
        @SerializedName("total_campaign_finished")
        @Expose val totalCampaignFinished: Int = 0,
        @SerializedName("use_restriction_engine")
        @Expose val useRestrictionEngine: Boolean = false
)

data class CampaignListV2(
        @SerializedName("campaign_id")
        @Expose val campaignId: String = "",
        @SerializedName("campaign_name")
        @Expose val campaignName: String = "",
        @SerializedName("campaign_type_id")
        @Expose val campaignTypeId: String = "",
        @SerializedName("campaign_type_name")
        @Expose val campaignTypeName: String = "",
        @SerializedName("status_id")
        @Expose val statusId: String = "",
        @SerializedName("status_text")
        @Expose val statusText: String = "",
        @SerializedName("status_detail")
        @Expose val statusDetail: String = "",
        @SerializedName("start_date")
        @Expose val startDate: String = "",
        @SerializedName("end_date")
        @Expose val endDate: String = "",
        @SerializedName("submission_start_date")
        @Expose val submissionStartDate: String = "",
        @SerializedName("submission_end_date")
        @Expose val submissionEndDate: String = "",
        @SerializedName("submission_end_time_diff")
        @Expose val submissionEndTimeDiff: Int = 0,
        @SerializedName("review_start_date")
        @Expose val reviewStartDate: String = "",
        @SerializedName("review_end_date")
        @Expose val reviewEndDate: String = "",
        @SerializedName("min_requirement")
        @Expose val minRequirement: String = "",
        @SerializedName("slug")
        @Expose val slug: String = "",
        @SerializedName("description")
        @Expose val description: String = "",
        @SerializedName("cover_img")
        @Expose val coverImg: String = "",
        @SerializedName("dashboard_url")
        @Expose val dashboardUrl: String = "",
        @SerializedName("product_criteria")
        @Expose val productCriteria: List<String> = listOf(),
        @SerializedName("seller_campaign_info")
        @Expose val sellerCampaignInfo: SellerCampaignInfo,
        @SerializedName("start_date_unix")
        @Expose val startDateUnix: Int = 0,
        @SerializedName("end_date_unix")
        @Expose val endDateUnix: Int = 0,
        @SerializedName("max_product_submission")
        @Expose val maxProductSubmission: Int = 0,
        @SerializedName("campaign_relation")
        @Expose val campaignRelation: List<String> = listOf(),
        @SerializedName("finished_widget_time")
        @Expose val finishedWidgetTime: String = "",
        @SerializedName("finished_widget_time_in_mins")
        @Expose val finishedWidgetTimeInMins: Int = 0,
        @SerializedName("total_notify")
        @Expose val totalNotify: Int = 0,
        @SerializedName("campaign_banner")
        @Expose val campaignBanner: List<CampaignBanner>,
        @SerializedName("remaining_quota")
        @Expose val remainingQuota: Int = 0,
        @SerializedName("is_set")
        @Expose val isSet: Boolean = false,
        @SerializedName("campaign_dynamic_rule")
        @Expose val campaignDynamicRule: CampaignDynamicRule
)

data class SellerCampaignInfo(
        @SerializedName("AcceptedProduct")
        @Expose val AcceptedProduct: Int = 0,
        @SerializedName("RejectedProduct")
        @Expose val RejectedProduct: Int = 0,
        @SerializedName("TotalItem")
        @Expose val TotalItem: Int = 0,
        @SerializedName("SoldItem")
        @Expose val SoldItem: Int = 0,
        @SerializedName("OnBookingItem")
        @Expose val OnBookingItem: Int = 0,
        @SerializedName("TotalRevenueSoldItem")
        @Expose val TotalRevenueSoldItem: Int = 0,
        @SerializedName("TotalRevenueOnBookingItem")
        @Expose val TotalRevenueOnBookingItem: Int = 0,
        @SerializedName("RegisteredProduct")
        @Expose val RegisteredProduct: Int = 0,
        @SerializedName("SubmittedProduct")
        @Expose val SubmittedProduct: Int = 0
)

data class CampaignBanner(
        @SerializedName("banner_id")
        @Expose val bannerId: String = "",
        @SerializedName("file_path")
        @Expose val filePath: String = "",
        @SerializedName("file_name")
        @Expose val fileName: String = "",
        @SerializedName("position")
        @Expose val position: Int = 0,
        @SerializedName("device")
        @Expose val device: String = "",
        @SerializedName("campaign_id")
        @Expose val campaignId: String = "",
        @SerializedName("redirect_url")
        @Expose val redirectUrl: String = "",
        @SerializedName("redirect_url_app")
        @Expose val redirectUrlApp: String = "",
        @SerializedName("banner_name")
        @Expose val bannerName: String = "",
        @SerializedName("banner_type")
        @Expose val bannerType: String = ""
)

data class CampaignDynamicRule(
        @SerializedName("dynamic_role_data")
        @Expose val dynamicRoleData: List<DynamicRoleData> = listOf(),
        @SerializedName("cta_link")
        @Expose val ctaLink: String = ""
)

data class DynamicRoleData(
        @SerializedName("rule_id")
        @Expose val ruleId: String = ""
)
