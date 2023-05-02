package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BulkReviewSubmitResponse(
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("productrevBulkSubmitProductReview")
        @Expose
        val productRevBulkSubmitProductReview: ProductRevBulkSubmitProductReview = ProductRevBulkSubmitProductReview()
    ) {
        data class ProductRevBulkSubmitProductReview(
            @SerializedName("success")
            @Expose
            val success: Boolean = false,
            @SerializedName("failedInboxIDs")
            @Expose
            val failedInboxIDs: List<String> = emptyList()
        )
    }
}
