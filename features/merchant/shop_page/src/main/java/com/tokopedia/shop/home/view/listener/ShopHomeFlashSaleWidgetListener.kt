package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeFlashSaleWidgetListener {

    fun onClickTncFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onClickSeeAllFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onClickFlashSaleReminder(model: ShopHomeFlashSaleUiModel)

    fun onTimerFinished(model: ShopHomeFlashSaleUiModel)

    fun onFlashSaleProductClicked(model: ShopHomeProductUiModel)

    fun onFlashSaleWidgetImpressed(model: ShopHomeFlashSaleUiModel, position: Int)

    fun onPlaceHolderClickSeeAll(model: ShopHomeFlashSaleUiModel)

    fun onProductAtcNonVariantQuantityEditorChanged(
        shopHomeProductViewModel: ShopHomeProductUiModel,
        quantity: Int
    )

    fun onProductAtcVariantClick(shopHomeProductViewModel: ShopHomeProductUiModel)

    fun onProductAtcDefaultClick(shopHomeProductViewModel: ShopHomeProductUiModel, quantity: Int)

    fun onImpressionProductAtc(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        position: Int,
        name: String
    )
}