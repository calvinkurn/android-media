package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel

interface FeedListener {
    fun onMenuClicked(id: String)
    fun onFollowClicked(id: String, encryptedId: String, isShop: Boolean)
    fun changeTab()
    fun reload()
    fun onReminderClicked(campaignId: Long, setReminder: Boolean)
    fun onTimerFinishUpcoming()
    fun onTimerFinishOnGoing()

    fun onProductTagButtonClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean
    )

    fun onProductTagViewClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int
    )

    fun onSharePostClicked(
        id: String,
        authorName: String,
        applink: String,
        weblink: String,
        imageUrl: String
    )

    fun onLikePostCLicked(id: String, isLiked: Boolean, rowNumber: Int)
}
