package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedInterestUser(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),

        @SerializedName("interests")
        @Expose
        val interests: List<InterestsItem> = ArrayList(),

        @SerializedName("error")
        @Expose
        val error: String = ""
)