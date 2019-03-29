package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateInterestData(
        @SerializedName("feed_interest_user_update")
        @Expose
        val feedInterestUserUpdate: FeedInterestUserUpdate = FeedInterestUserUpdate()
)