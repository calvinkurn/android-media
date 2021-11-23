package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 9/28/17.
 */
data class ReplyReviewPojo(
    @SerializedName("is_success")
    @Expose
    val isSuccess: Int = 0
)