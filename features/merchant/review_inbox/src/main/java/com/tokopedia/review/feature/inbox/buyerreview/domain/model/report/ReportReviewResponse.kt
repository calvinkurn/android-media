package com.tokopedia.review.feature.inbox.buyerreview.domain.model.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportReviewResponse(
    @SerializedName("productrevReportReview")
    @Expose
    val productrevReportReview: ProductrevReportReview = ProductrevReportReview()
) {
    data class ProductrevReportReview(
        @SerializedName("success")
        @Expose
        val success: Boolean = false
    )
}
