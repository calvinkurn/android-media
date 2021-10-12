package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel

interface ShopHomeFlashSaleWidgetListener {

    fun onClickTncFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onTimerFinished(model: ShopHomeFlashSaleUiModel)
}