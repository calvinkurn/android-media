package com.tokopedia.shop.home.view.listener;

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
