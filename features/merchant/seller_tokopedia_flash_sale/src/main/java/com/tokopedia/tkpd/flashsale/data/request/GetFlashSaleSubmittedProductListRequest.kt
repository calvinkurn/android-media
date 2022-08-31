package com.tokopedia.tkpd.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleSubmittedProductListRequest(
    @SerializedName("request_header")
    val requestHeader: SubmittedProductListRequestHeader = SubmittedProductListRequestHeader(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("campaign_id")
    val campaignId: Int = 0,
    @SerializedName("pagination")
    val pagination: Pagination = Pagination(),
    @SerializedName("filter")
    val filter: Filter = Filter()
) {

    data class SubmittedProductListRequestHeader(
        @SerializedName("usecase")
        val usecase: String = ""
    )

    data class Pagination(
        @SerializedName("rows")
        val rows: Int = 10,
        @SerializedName("offset")
        val offset: Int = 0
    )

    data class Filter(
        @SuppressLint("Invalid Data Type")
        @SerializedName("keyword")
        val keyword: String = "",
        @SerializedName("category_ids")
        val categoryIds: Int? = null
    )
}
