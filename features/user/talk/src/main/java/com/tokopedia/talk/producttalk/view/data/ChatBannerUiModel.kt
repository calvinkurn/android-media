package com.tokopedia.talk.producttalk.view.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkListTypeFactory

data class ChatBannerUiModel(
        val productId: String = "",
        val productImageUrl: String = "",
        val productName: String = "",
        val productPrice: String = "",
        val productUrl: String = ""
) : Visitable<ProductTalkListTypeFactory> {

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }

}
