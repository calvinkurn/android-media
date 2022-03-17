package com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener

import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.SingleProductBundlingUiModel

interface ProductBundlingListener {
    fun onClickCtaProductBundling(element: SingleProductBundlingUiModel)
    fun onSeenProductBundling(element: SingleProductBundlingUiModel)

    fun onClickCtaMultipleProductBundling(element: MultipleProductBundlingUiModel)
    fun onSeenMultipleProductBundling(element: MultipleProductBundlingUiModel)
}