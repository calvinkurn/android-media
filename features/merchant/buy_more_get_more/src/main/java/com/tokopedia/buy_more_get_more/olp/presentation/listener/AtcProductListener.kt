package com.tokopedia.buy_more_get_more.olp.presentation.listener

import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel

interface AtcProductListener {
    fun onProductAtcVariantClicked(product: OfferProductListUiModel.Product)
    fun onProductCardClicked(product: OfferProductListUiModel.Product)
    fun onProductImpressed(product: OfferProductListUiModel.Product, position: Int)
}
