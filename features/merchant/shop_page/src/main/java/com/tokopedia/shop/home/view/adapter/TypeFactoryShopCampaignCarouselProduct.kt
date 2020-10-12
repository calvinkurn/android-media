package com.tokopedia.shop.home.view.adapter

import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

interface TypeFactoryShopCampaignCarouselProduct {
    fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int
    fun type(uiModel: ShopHomeCampaignCarouselClickableBannerAreaUiModel): Int
}