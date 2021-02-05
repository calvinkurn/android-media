package com.tokopedia.review.feature.inbox.common.domain.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReviewTabCounterResponse(
    @SerializedName("productrevReviewTabCounter")
    @Expose
    val productrevReviewTabCounter: ProductReviewTabCounter = ProductReviewTabCounter()
) {
    data class ProductReviewTabCounter(
            @SerializedName("list")
            @Expose
            val list: List<TabCounter> = listOf()
    ) {
        data class TabCounter (
                @SerializedName("count")
                @Expose
                val count: Int? = 0,
                @SerializedName("tabName")
                @Expose
                val tabName: String? = ""
        )
    }
}