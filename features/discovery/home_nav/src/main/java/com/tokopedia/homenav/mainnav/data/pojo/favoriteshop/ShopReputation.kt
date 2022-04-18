package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ShopReputation(
    @SerializedName("tooltip")
    @Expose
    val tooltip: String? = "",
    @SerializedName("reputation_score")
    @Expose
    val reputationScore: String? = "",
    @SerializedName("score")
    @Expose
    val score: Int? = 0,
    @SerializedName("min_badge_score")
    @Expose
    val minBadgeScore: Int? = 0,
    @SerializedName("badge")
    @Expose
    val badge: String? = "",
    @SerializedName("badge_level")
    @Expose
    val badgeLevel: Int? = 0
)
