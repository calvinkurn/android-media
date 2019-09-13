package com.tokopedia.feedplus.profilerecommendation.view.viewmodel

/**
 * Created by jegul on 2019-09-13.
 */
data class FollowRecommendationCardViewModel(
        val header: String?,
        val image1Url: String,
        val image2Url: String,
        val image3Url: String,
        val avatar: String,
        val title: String,
        val description: String,
        val badgeUrl: String,
        val enabledFollowText: String,
        val disabledFollowText: String,
        val isFollowed: Boolean,
        val authorId: String
)