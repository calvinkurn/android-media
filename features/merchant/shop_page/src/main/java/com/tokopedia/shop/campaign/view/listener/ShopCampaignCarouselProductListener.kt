package com.tokopedia.shop.campaign.view.listener

import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopCampaignCarouselProductListener {
    fun onCampaignCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    )

    fun onCampaignCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    )

    fun onCtaClicked(carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?)

    fun onCampaignCarouselProductWidgetImpression(
        position: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel
    )

}
