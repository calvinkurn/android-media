package com.tokopedia.tkpd.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleSubmittedProductListRequest(
    @SerializedName("request_header")
    val requestHeader: SubmittedProductListRequestHeader = SubmittedProductListRequestHeader(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("campaign_id")
    val campaignId: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("pagination")
    val pagination: Pagination = Pagination(),
    @SerializedName("filter")
    val filter: Filter = Filter()
) {

    data class SubmittedProductListRequestHeader(
        @SerializedName("source")
        val source: String = "fe",
        @SerializedName("usecase")
        val usecase: String = "list"
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
        val categoryIds: List<Int> = listOf()
    )
}
