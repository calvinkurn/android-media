package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedCardModel

interface FeedListener {
    fun onMenuClicked(model: FeedCardModel)
    fun onProductTagItemClicked(model: FeedCardModel)
    fun onProductTagViewClicked(model: FeedCardModel)
    fun disableClearView()
    fun inClearViewMode(): Boolean
    fun onSharePostClicked(model: FeedCardModel)

}
