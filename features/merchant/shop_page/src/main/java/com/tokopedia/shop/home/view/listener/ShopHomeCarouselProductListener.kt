package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeCarouselProductListener {
    fun onCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onPersonalizationCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onPersonalizationReminderCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductPersonalizationItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductPersonalizationReminderItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?)

    fun onCarouselProductItemClickAddToCart(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCarouselPersonalizationProductItemClickAddToCart(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?,
            isOcc: Boolean = false
    )

    fun onCarouselPersonalizationReminderProductItemClickAddToCart(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onThreeDotsCarouselProductItemClicked(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductShowcaseItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductUiModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductShowcaseItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductUiModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductShowcaseItemClickAddToCart(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductUiModel: ShopHomeProductUiModel?
    )

    fun onCarouselProductShowcaseCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?)

    fun onCarouselProductWidgetImpression(adapterPosition: Int, model: ShopHomeCarousellProductUiModel)

    fun onProductAtcNonVariantQuantityEditorChanged(
        shopHomeProductViewModel: ShopHomeProductUiModel,
        quantity: Int
    )

    fun onProductAtcVariantClick(shopHomeProductViewModel: ShopHomeProductUiModel)

    fun onProductAtcDefaultClick(shopHomeProductViewModel: ShopHomeProductUiModel)

}
