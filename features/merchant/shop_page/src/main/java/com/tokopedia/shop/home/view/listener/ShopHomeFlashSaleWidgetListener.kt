package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel

interface ShopHomeFlashSaleWidgetListener {

    fun onClickTncFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onClickSeeAllFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onClickFlashSaleReminder(model: ShopHomeFlashSaleUiModel)

    fun onTimerFinished(model: ShopHomeFlashSaleUiModel)

    fun onFlashSaleProductClicked(model: ShopHomeProductUiModel)
}