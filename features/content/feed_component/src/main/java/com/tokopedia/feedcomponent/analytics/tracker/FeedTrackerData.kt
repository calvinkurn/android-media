package com.tokopedia.feedcomponent.analytics.tracker

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct

data class FeedTrackerData(
    val postId: String,
    val media: FeedXMedia = FeedXMedia(),
    val postType: String,
    val isFollowed: Boolean,
    val shopId: String,
    val mediaType: String = "",
    val positionInFeed: Int = 0,
    val contentSlotValue: String,
    val campaignStatus: String = "",
    val trackerId: String = "",
    val productId: String = "",
    val product: FeedXProduct = FeedXProduct(),
    val mediaIndex: Int = 0,
    val isProductDetailPage: Boolean = false,
    val hasVoucher: Boolean = false,
    val authorType: String = ""
)
