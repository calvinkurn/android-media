package com.tokopedia.shop.home.view.listener;

import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface ShopHomeCampaignNplWidgetListener {
    fun onCampaignCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onCampaignCarouselProductItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
            shopHomeProductViewModel: ShopHomeProductUiModel?
    )

    fun onClickTncCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel)

    fun onClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel)

    fun onClickCtaCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel)

    fun onClickCampaignBannerAreaNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel)

    fun onImpressionCampaignNplWidget(
            position: Int,
            shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel
    )

    fun onTimerFinished(model: ShopHomeNewProductLaunchCampaignUiModel)
}
