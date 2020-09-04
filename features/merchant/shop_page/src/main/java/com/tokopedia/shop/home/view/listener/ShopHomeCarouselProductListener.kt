package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

interface ShopHomeCarouselProductListener {
    fun onCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

    fun onCarouselProductItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

    fun onCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?)

    fun onCarouselProductItemClickAddToCart(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

    fun onThreeDotsCarouselProductItemClicked(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )
}
