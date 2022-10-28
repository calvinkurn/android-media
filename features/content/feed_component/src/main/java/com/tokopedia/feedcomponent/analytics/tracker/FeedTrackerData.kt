package com.tokopedia.feedcomponent.analytics.tracker

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct

data class FeedTrackerData(
    val postId: String,
    val media: FeedXMedia,
    val postType: String,
    val isFollowed: Boolean,
    val shopId: String,
    val mediaType: String,
    val positionInFeed: Int,
    val contentSlotValue: String,
    val campaignStatus: String,
    val trackerId: String,
    val productId: String,
    val product : FeedXProduct,
    val mediaIndex: Int
)
