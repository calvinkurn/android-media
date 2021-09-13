package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 9/13/17.
 */
data class ReportReviewPojo(
    @SerializedName("is_success")
    @Expose
    var isSuccess: Int = 0
)