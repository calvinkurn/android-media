package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationHeader(
        @SerializedName("avatar")
        @Expose
        val avatar: String = "",

        @SerializedName("avatarTitle")
        @Expose
        val avatarTitle: String = "",

        @SerializedName("avatarWeblink")
        @Expose
        val avatarWeblink: String = "",

        @SerializedName("avatarApplink")
        @Expose
        val avatarApplink: String = "",

        @SerializedName("avatarBadgeImage")
        @Expose
        val avatarBadgeImage: String = "",

        @SerializedName("avatarDescription")
        @Expose
        val avatarDescription: String = "",

        @SerializedName("followCta")
        @Expose
        val followCta: FollowRecommendationFollowCta = FollowRecommendationFollowCta()
)