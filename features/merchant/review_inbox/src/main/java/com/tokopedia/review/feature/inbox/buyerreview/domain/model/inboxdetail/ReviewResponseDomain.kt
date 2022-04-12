package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ReviewResponseDomain (
    @SerializedName("message") @Expose val responseMessage: String,
    @SerializedName("createTime") @Expose val responseCreateTime: ResponseCreateTimeDomain,
)