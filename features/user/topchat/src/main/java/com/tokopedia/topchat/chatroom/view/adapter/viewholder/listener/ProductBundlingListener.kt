package com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener

import com.tokopedia.topchat.chatroom.view.uimodel.ProductBundlingUiModel

interface ProductBundlingListener {
    fun onClickCtaProductBundling(element: ProductBundlingUiModel)
    fun onSeenProductBundling(isMultiProduct: Boolean)
}