package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel

interface FeedListener {
    fun onMenuClicked(id: String)
    fun onProductTagItemClicked(model: FeedCardImageContentModel)
    fun onProductTagViewClicked(model: FeedCardImageContentModel)
    fun disableClearView()
    fun inClearViewMode(): Boolean
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
