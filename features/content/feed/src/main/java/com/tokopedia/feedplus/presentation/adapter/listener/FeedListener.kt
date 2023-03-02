package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel

interface FeedListener {
    fun onMenuClicked(model: FeedCardImageContentModel)

    fun disableClearView()
    fun inClearViewMode(): Boolean
}
