package com.tokopedia.officialstore.official.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class OfficialHomeAdapterTypeFactory(
        private val recommendationListener: RecommendationListener,
        private val dcEventHandler: DynamicChannelEventHandler,
        private val featuredShopListener: FeaturedShopListener
) : BaseAdapterTypeFactory(), OfficialHomeTypeFactory {

    override fun type(officialBannerViewModel: OfficialBannerViewModel): Int {
        return if (officialBannerViewModel.banner.isEmpty())
            HideViewHolder.LAYOUT
        else
            OfficialBannerViewHolder.LAYOUT
    }
    override fun type(officialBenefitViewModel: OfficialBenefitViewModel): Int {
        return if (officialBenefitViewModel.benefit.isEmpty())
            HideViewHolder.LAYOUT
        else
            OfficialBenefitViewHolder.LAYOUT
    }

    override fun type(officialFeaturedShopViewModel: OfficialFeaturedShopViewModel): Int {
        return if (officialFeaturedShopViewModel.featuredShop.isEmpty())
            HideViewHolder.LAYOUT
        else
            OfficialFeaturedShopViewHolder.LAYOUT
    }

    override fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int {
        return dynamicChannelViewModel.getLayoutType()
    }

    override fun type(productRecommendationTitleViewModel: ProductRecommendationTitleViewModel): Int {
        return ProductRecommendationTitleViewHolder.LAYOUT
    }

    override fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OfficialBannerViewHolder.LAYOUT -> OfficialBannerViewHolder(parent)
            OfficialBenefitViewHolder.LAYOUT -> OfficialBenefitViewHolder(parent)
            OfficialFeaturedShopViewHolder.LAYOUT -> OfficialFeaturedShopViewHolder(parent, featuredShopListener)
            DynamicChannelLegoViewHolder.LAYOUT -> DynamicChannelLegoViewHolder(parent, dcEventHandler)
            DynamicChannelThematicViewHolder.LAYOUT -> DynamicChannelThematicViewHolder(parent, dcEventHandler)
            DynamicChannelSprintSaleViewHolder.LAYOUT -> DynamicChannelSprintSaleViewHolder(parent, dcEventHandler)
            ProductRecommendationTitleViewHolder.LAYOUT -> ProductRecommendationTitleViewHolder(parent)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(parent, recommendationListener)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
