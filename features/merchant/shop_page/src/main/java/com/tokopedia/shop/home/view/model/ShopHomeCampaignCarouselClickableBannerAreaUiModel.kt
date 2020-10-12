package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.product.view.datamodel.LabelGroupViewModel
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct

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
