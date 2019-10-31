package com.tokopedia.officialstore.official.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelThematicViewHolder
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelLegoViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelSprintSaleViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class OfficialHomeAdapterTypeFactory(
        private val recommendationListener: RecommendationListener,
        private val countDownListener: CountDownView.CountDownListener
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
            OfficialFeaturedShopViewHolder.LAYOUT -> OfficialFeaturedShopViewHolder(parent)
            DynamicChannelLegoViewHolder.LAYOUT -> DynamicChannelLegoViewHolder(parent)
            DynamicChannelThematicViewHolder.LAYOUT -> DynamicChannelThematicViewHolder(parent)
            DynamicChannelSprintSaleViewHolder.LAYOUT -> DynamicChannelSprintSaleViewHolder(parent, countDownListener)
            ProductRecommendationTitleViewHolder.LAYOUT -> ProductRecommendationTitleViewHolder(parent)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(parent, recommendationListener)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
