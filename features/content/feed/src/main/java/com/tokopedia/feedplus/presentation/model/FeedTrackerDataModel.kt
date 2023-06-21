package com.tokopedia.feedplus.presentation.model

import com.tokopedia.feedplus.presentation.model.type.AuthorType

/**
 * Created By : Muhammad Furqan on 18/04/23
 */
data class FeedTrackerDataModel(
    val activityId: String,
    val authorId: String,
    val tabType: String,
    val typename: String,
    val type: String,
    val authorType: AuthorType,
    val mediaType: String,
    val isFollowing: Boolean,
    val contentScore: String,
    val hasVoucher: Boolean,
    val campaignStatus: String,
    val entryPoint: String
)
