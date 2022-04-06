package com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener

import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel

interface ProductBundlingListener {
    fun onClickCtaProductBundling(element: ProductBundlingUiModel)
    fun onSeenProductBundling(element: ProductBundlingUiModel)
}