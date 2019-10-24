package com.tokopedia.officialstore.official.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class OfficialHomeAdapterTypeFactory(
        val recommendationListener: RecommendationListener
) : BaseAdapterTypeFactory(), OfficialHomeTypeFactory {

    override fun type(officialBannerViewModel: OfficialBannerViewModel): Int {
        return OfficialBannerViewHolder.LAYOUT
    }

    override fun type(officialFeaturedShopViewModel: OfficialFeaturedShopViewModel): Int {
        return OfficialFeaturedShopViewHolder.LAYOUT
    }

    override fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int {
        return DynamicChannelViewHolder.LAYOUT
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
            OfficialFeaturedShopViewHolder.LAYOUT -> OfficialFeaturedShopViewHolder(parent)
            DynamicChannelViewHolder.LAYOUT -> DynamicChannelViewHolder(parent)
            ProductRecommendationTitleViewHolder.LAYOUT -> ProductRecommendationTitleViewHolder(parent)
            ProductRecommendationViewHolder.LAYOUT -> ProductRecommendationViewHolder(parent, recommendationListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}