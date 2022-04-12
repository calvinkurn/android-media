package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/30/17.
 */
class ReviewUpdateTimeDomain constructor(
    @SerializedName("dateTimeFmt") @Expose val dateTimeFmt1: String = ""
)