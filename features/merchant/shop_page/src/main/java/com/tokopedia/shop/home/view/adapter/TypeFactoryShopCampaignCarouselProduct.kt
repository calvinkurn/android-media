package com.tokopedia.shop.home.view.adapter

import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

interface TypeFactoryShopCampaignCarouselProduct {
    fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int
}