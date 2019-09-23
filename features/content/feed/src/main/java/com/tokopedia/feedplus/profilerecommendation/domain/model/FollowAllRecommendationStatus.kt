package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-16.
 */
data class FollowAllRecommendationStatus(

        @SerializedName("success")
        @Expose
        val isSuccess: Boolean = false,

        @SerializedName("count")
        @Expose
        val count: Int = 0,

        @SerializedName("source")
        @Expose
        val source: String = ""
)