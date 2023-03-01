package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedCardModel

interface FeedListener {
    fun onMenuClicked(model: FeedCardModel)
    fun onProductTagClicked(model: FeedCardModel)
    fun disableClearView()
    fun inClearViewMode(): Boolean
}
