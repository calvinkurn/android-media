package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationAsset(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("instruction")
        @Expose
        val instruction: String = "",

        @SerializedName("buttonCta")
        @Expose
        val buttonCta: String = "",

        @SerializedName("shopDescription")
        @Expose
        val shopDescription: String = "",

        @SerializedName("profileDescription")
        @Expose
        val profileDescription: String = ""
)