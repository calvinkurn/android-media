package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel

interface FeedListener {
    fun onMenuClicked(id: String)
    fun disableClearView()
    fun inClearViewMode(): Boolean
    fun onFollowClicked(id: String, isShop: Boolean)
    fun changeTab(type: String)
    fun reload()

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
    fun onFollowClicked(id: String, isShop: Boolean)
    fun changeTab(type: String)
    fun reload()
    fun onLikePostCLicked(model: FeedCardImageContentModel, rowNumber: Int)

}
