package com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener

import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.BundleItem
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel

interface ProductBundlingListener {
    fun onClickCtaProductBundling(element: ProductBundlingUiModel)
    fun onSeenProductBundling(element: ProductBundlingUiModel)
    fun onClickProductBundlingImage(item: BundleItem, element: ProductBundlingUiModel)
}