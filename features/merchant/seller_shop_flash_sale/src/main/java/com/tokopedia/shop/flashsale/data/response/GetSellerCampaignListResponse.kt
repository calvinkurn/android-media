package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetSellerCampaignListResponse(
    @SerializedName("getSellerCampaignList")
    val getSellerCampaignList: GetSellerCampaignList = GetSellerCampaignList()
) {
    data class GetSellerCampaignList(
        @SerializedName("campaign")
        val campaign: List<Campaign> = listOf(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("total_campaign")
        val totalCampaign: Int = 0,
        @SerializedName("total_campaign_active")
        val totalCampaignActive: Int = 0,
        @SerializedName("total_campaign_finished")
        val totalCampaignFinished: Int = 0
    ) {
        data class Campaign(
            @SerializedName("bitmask_is_set")
            val bitmaskIsSet: Boolean = false,
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("campaign_name")
            val campaignName: String = "",
            @SerializedName("campaign_type_id")
            val campaignTypeId: String = "",
            @SerializedName("campaign_type_name")
            val campaignTypeName: String = "",
            @SerializedName("cover_img")
            val coverImg: String = "",
            @SerializedName("end_date")
            val endDate: Long = 0,
            @SerializedName("etalase_prefix")
            val etalasePrefix: String = "",
            @SerializedName("finished_widget_time")
            val finishedWidgetTime: Long = 0,
            @SerializedName("finished_widget_time_in_mins")
            val finishedWidgetTimeInMins: Int = 0,
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
            @SerializedName("redirect_url")
            val redirectUrl: String = "",
            @SerializedName("redirect_url_app")
            val redirectUrlApp: String = "",
            @SerializedName("review_end_date")
            val reviewEndDate: Long = 0,
            @SerializedName("review_start_date")
            val reviewStartDate: Long = 0,
            @SerializedName("start_date")
            val startDate: Long = 0,
            @SerializedName("status_detail")
            val statusDetail: String = "",
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
            @SerializedName("use_upcoming_widget")
            val useUpcomingWidget: Boolean = false,
            @SerializedName("product_summary")
            val productSummary: ProductSummary = ProductSummary()
        ) {
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

        }

        data class ResponseHeader(
            @SerializedName("processTime")
            val processTime: Double = 0.0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}