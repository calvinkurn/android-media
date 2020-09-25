package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

class ShopCampaignCarouselProductAdapterTypeFactory(
        private val shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener,
        private val parentPosition: Int
) : BaseAdapterTypeFactory(), TypeFactoryShopCampaignCarouselProduct {

    var adapter: ShopCampaignCarouselProductAdapter? = null

    override fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int {
        return ShopHomeCampaignCarouselProductItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHomeCampaignCarouselProductItemViewHolder.LAYOUT -> {
                ShopHomeCampaignCarouselProductItemViewHolder(
                        parent,
                        parentPosition,
                        shopHomeNewProductLaunchCampaignUiModel,
                        shopHomeCampaignNplWidgetListener
                )
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }

}