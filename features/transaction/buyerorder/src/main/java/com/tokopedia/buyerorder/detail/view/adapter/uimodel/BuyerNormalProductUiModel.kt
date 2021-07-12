package com.tokopedia.buyerorder.detail.view.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.detail.view.adapter.typefactory.BuyerProductBundlingTypeFactory

class BuyerNormalProductUiModel(val productName: String,
                                val productPrice: String,
                                val productThumbnailUrl: String)
    : Visitable<BuyerProductBundlingTypeFactory> {

    override fun type(typeFactory: BuyerProductBundlingTypeFactory): Int = typeFactory.type(this)

}