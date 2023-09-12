package com.tokopedia.review.feature.bulkreview

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BulkReviewRecommendationWidget(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("list")
    @Expose
    val list: List<Detail> = emptyList(),

    @SerializedName("productCount")
    @Expose
    val productCount: String = "",

    @SerializedName("productCountFmt")
    @Expose
    val productCountFmt: String = "",

    @SerializedName("linkDetail")
    @Expose
    val linkDetail: LinkDetail = LinkDetail()
) {

    data class Detail(
        @SerializedName("product")
        @Expose
        val product: Product
    )

    data class Product(
        @SerializedName("imageURL")
        @Expose
        val imageUrl: String = ""
    )

    data class LinkDetail(
        @SerializedName("appLink")
        @Expose
        val appLink: String = ""
    )
}

data class BulkReviewRecommendationWidgetResponse(
    @SerializedName("productrevGetBulkReviewRecommendationWidget")
    @Expose
    val bulkReviewRecommendationWidget: BulkReviewRecommendationWidget = BulkReviewRecommendationWidget()
)
