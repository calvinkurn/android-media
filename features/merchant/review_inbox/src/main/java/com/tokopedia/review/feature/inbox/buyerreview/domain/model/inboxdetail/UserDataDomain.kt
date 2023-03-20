package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class UserDataDomain constructor(
    @SerializedName("userID") @Expose val userId: String = "",
    @SerializedName("fullName") @Expose val fullName: String = ""
)
