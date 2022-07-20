package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.typefactory

import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerNormalProductUiModel

interface BuyerProductBundlingTypeFactory {

    fun type(uiModel: BuyerNormalProductUiModel): Int

}