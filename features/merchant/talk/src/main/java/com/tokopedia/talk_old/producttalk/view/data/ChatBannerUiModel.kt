package com.tokopedia.talk_old.producttalk.view.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.producttalk.view.adapter.ProductTalkListTypeFactory

data class ChatBannerUiModel(
        val productId: String = "",
        val productImageUrl: String = "",
        val productName: String = "",
        val productPrice: String = "",
        val productUrl: String = "",
        val shopId: String = "",
        val shopName: String = ""
) : Visitable<ProductTalkListTypeFactory> {

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }

}
