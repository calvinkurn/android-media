package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("feed_interest_user")
        @Expose
        val feedInterestUser: FeedInterestUser = FeedInterestUser()
)