package com.tokopedia.play.broadcaster.domain.model.campaign

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 25/01/22.
 */
@SuppressLint("ResponseFieldAnnotation", "Invalid Data Type")
data class GetCampaignListResponse(
    @SerializedName("getSellerCampaignList")
    val getSellerCampaignList: GetSellerCampaignList = GetSellerCampaignList()
) {

    data class GetSellerCampaignList(
        @SerializedName("campaign")
        val campaigns: List<Campaign> = emptyList(),
    )

    data class Campaign(
        @SerializedName("campaign_id")
        val campaignId: String = "",

        @SerializedName("campaign_name")
        val campaignName: String = "",

        @SerializedName("start_date")
        val startDate: Long = 0L,

        @SerializedName("end_date")
        val endDate: Long = 0L,

        @SerializedName("status_id")
        val statusId: Int = -1,

        @SerializedName("status_text")
        val statusText: String = "",

        @SerializedName("cover_img")
        val coverImage: String = "",

        @SerializedName("product_summary")
        val productSummary: ProductSummary = ProductSummary(),
    )

    data class ProductSummary(
        @SerializedName("total_item")
        val totalItem: Int = 0,
    )
}