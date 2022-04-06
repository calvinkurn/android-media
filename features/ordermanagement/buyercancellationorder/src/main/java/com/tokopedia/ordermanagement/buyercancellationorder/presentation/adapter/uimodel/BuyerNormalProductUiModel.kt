package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.typefactory.BuyerProductBundlingTypeFactory

class BuyerNormalProductUiModel(val productId: String,
                                val productName: String,
                                val productPrice: String,
                                val productThumbnailUrl: String)
    : Visitable<BuyerProductBundlingTypeFactory> {

    override fun type(typeFactory: BuyerProductBundlingTypeFactory): Int = typeFactory.type(this)

}