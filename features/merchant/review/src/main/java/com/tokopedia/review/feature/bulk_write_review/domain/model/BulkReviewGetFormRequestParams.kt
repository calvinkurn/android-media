package com.tokopedia.review.feature.bulk_write_review.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class BulkReviewGetFormRequestParams(
    @SerializedName("invoiceNumber")
    @Expose
    val invoice: String,
    @SerializedName("utmSource")
    @Expose
    val utmSource: String
) : GqlParam
