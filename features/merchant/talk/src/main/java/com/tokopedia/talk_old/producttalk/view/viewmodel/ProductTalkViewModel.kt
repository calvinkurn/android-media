package com.tokopedia.talk_old.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.producttalk.view.data.ChatBannerUiModel


/**
 * @author by Steven.
 */

data class ProductTalkViewModel(
        var screen: String = "",
        var listThread: ArrayList<Visitable<*>> = ArrayList(),
        var hasNextPage: Boolean = false,
        var page_id: Int = 0,
        var productId: Int = 0,
        var productName: String = "",
        var productImage: String = "",
        var productUrl: String = "",
        var productPrice: String = "",
        var shopId: Int = 0,
        var shopName: String = "",
        var shopAvatar: String = ""
) {

    fun addChatTicker() {
        val chatBanner = ChatBannerUiModel(
                productId.toString(), productImage, productName,
                productPrice, productUrl, shopId.toString(), shopName
        )
        listThread.add(0, chatBanner)
    }

}
