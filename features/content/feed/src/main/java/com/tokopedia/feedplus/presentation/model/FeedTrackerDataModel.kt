package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 18/04/23
 */
data class FeedTrackerDataModel(
    val activityId: String,
    val authorId: String,
    val tabType: String,
    val typename: String,
    val type: String,
    val authorType: Int,
    val mediaType: String,
    val isFollowing: Boolean,
    val contentScore: String,
    val hasVoucher: Boolean,
    val campaignStatus: String,
    val entryPoint: String
)
