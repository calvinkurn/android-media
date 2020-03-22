package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

interface ShopPageHomeProductClickListener {
    fun onAllProductItemClicked(
            itemPosition: Int,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

    fun onAllProductItemImpression(
            itemPosition: Int,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )

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

    fun onThreeDotsAllProductClicked(
            itemPosition: Int,
            shopHomeProductViewModel: ShopHomeProductViewModel
    )

    fun onThreeDotsCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    )
}
