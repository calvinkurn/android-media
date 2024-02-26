package com.tokopedia.shop_widget.buy_more_save_more.presentation.listener

import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product

interface BmsmWidgetItemEventListener {

    fun onAtcClicked(product: Product)

    fun onProductCardClicked(product: Product)

    fun onNavigateToOlp()

}
