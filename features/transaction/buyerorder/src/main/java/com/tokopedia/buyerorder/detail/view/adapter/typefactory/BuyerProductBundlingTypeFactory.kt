package com.tokopedia.buyerorder.detail.view.adapter.typefactory

import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerBundlingProductUiModel
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerNormalProductUiModel

interface BuyerProductBundlingTypeFactory {

    fun type(uiModel: BuyerNormalProductUiModel): Int
    fun type(uiModel: BuyerBundlingProductUiModel): Int

}