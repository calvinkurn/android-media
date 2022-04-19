package com.tokopedia.review.feature.credibility.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewerCredibilityStatsResponse(
    @SerializedName("productrevGetReviewerCredibilityStats")
    @Expose
    val response: ReviewerCredibilityStatsWrapper = ReviewerCredibilityStatsWrapper()
)

data class ReviewerCredibilityStatsWrapper(
    @SerializedName("label")
    @Expose
    val label: ReviewerCredibilityLabel = ReviewerCredibilityLabel(),
    @SerializedName("stats")
    @Expose
    val stats: List<ReviewerCredibilityStat> = listOf()
)

data class ReviewerCredibilityLabel(
    @SerializedName("userName")
    @Expose
    val userName: String = "",
    @SerializedName("joinDate")
    @Expose
    val joinDate: String = "",
    @SerializedName("subtitle")
    @Expose
    val subtitle: String = "",
    @SerializedName("footer")
    @Expose
    val footer: String = "",
    @SerializedName("ctaText")
    @Expose
    val ctaText: String = "",
    @SerializedName("ctaApplink")
    @Expose
    val applink: String = "",
    @SerializedName("infoText")
    @Expose
    val infoText: String = ""
)

data class ReviewerCredibilityStat(
    @SerializedName("key")
    @Expose
    val key: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("imageURL")
    @Expose
    val imageURL: String = "",
    @SerializedName("count")
    @Expose
    val count: Int = 0,
    @SerializedName("countFmt")
    @Expose
    val countFmt: String = "",
    @SerializedName("show")
    @Expose
    val shouldShow: Boolean = false
)