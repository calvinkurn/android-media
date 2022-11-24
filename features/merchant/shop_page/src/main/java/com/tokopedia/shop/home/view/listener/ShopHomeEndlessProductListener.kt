package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeEndlessProductListener {
    fun onAllProductItemClicked(
        itemPosition: Int,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onAllProductItemImpression(
        itemPosition: Int,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onThreeDotsAllProductClicked(shopHomeProductViewModel: ShopHomeProductUiModel)

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
