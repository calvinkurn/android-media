package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapterTypeFactory

/**
 * Created by nathan on 2/6/18.
 */

class ShopHomeCampaignCarouselClickableBannerAreaUiModel(
        val width: Int = 0
) : Visitable<ShopCampaignCarouselProductAdapterTypeFactory> {
    override fun type(typeFactory: ShopCampaignCarouselProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
