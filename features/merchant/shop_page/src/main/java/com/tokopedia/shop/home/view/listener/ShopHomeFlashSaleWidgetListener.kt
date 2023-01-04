package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeFlashSaleWidgetListener {

    fun onClickTncFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onClickSeeAllFlashSaleWidget(model: ShopHomeFlashSaleUiModel)

    fun onClickFlashSaleReminder(model: ShopHomeFlashSaleUiModel)

    fun onTimerFinished(model: ShopHomeFlashSaleUiModel)

    fun onFlashSaleProductClicked(model: ShopHomeProductUiModel, widgetModel: ShopHomeFlashSaleUiModel, position: Int)

    fun onFlashSaleWidgetImpressed(model: ShopHomeFlashSaleUiModel, position: Int)

    fun onPlaceHolderClickSeeAll(model: ShopHomeFlashSaleUiModel)

    fun onFlashSaleProductImpression(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        flashSaleUiModel: ShopHomeFlashSaleUiModel?,
        position: Int
    )

    fun onProductAtcNonVariantQuantityEditorChanged(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        quantity: Int,
        componentName: String
    )

    fun onProductAtcVariantClick(shopHomeProductUiModel: ShopHomeProductUiModel)

    fun onProductAtcDefaultClick(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        quantity: Int,
        componentName: String
    )

    fun onImpressionProductAtc(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        position: Int,
        name: String
    )
}
