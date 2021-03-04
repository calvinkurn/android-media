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
}
