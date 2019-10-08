package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationFollowCta(
        @SerializedName("textTrue")
        @Expose
        val textTrue: String = "",

        @SerializedName("textFalse")
        @Expose
        val textFalse: String = "",

        @SerializedName("isFollow")
        @Expose
        val isFollow: Boolean = false,

        @SerializedName("authorID")
        @Expose
        val authorID: String = "",

        @SerializedName("authorType")
        @Expose
        val authorType: String = ""
)