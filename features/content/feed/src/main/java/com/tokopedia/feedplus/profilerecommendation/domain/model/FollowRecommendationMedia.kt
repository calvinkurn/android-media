package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationMedia(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("appLink")
        @Expose
        val appLink: String = "",

        @SerializedName("webLink")
        @Expose
        val webLink: String = "",

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = "",

        @SerializedName("thumbnailLarge")
        @Expose
        val thumbnailLarge: String = ""
)