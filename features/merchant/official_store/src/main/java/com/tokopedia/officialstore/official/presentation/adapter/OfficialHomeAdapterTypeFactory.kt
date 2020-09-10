package com.tokopedia.officialstore.official.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.visitable.*
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

class OfficialHomeAdapterTypeFactory(
        private val recommendationListener: RecommendationListener,
        private val dcEventHandler: DynamicChannelEventHandler,
        private val featuredShopListener: FeaturedShopListener,
        private val homeComponentListener: HomeComponentListener,
        private val legoBannerListener: DynamicLegoBannerListener
) : BaseAdapterTypeFactory(), OfficialHomeTypeFactory, HomeComponentTypeFactory {

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
        return OfficialProductRecommendationTitleViewHolder.LAYOUT
    }

    override fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int {
        return OfficialProductRecommendationViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return OfficialLoadingContentViewHolder.LAYOUT
    }

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int {
        return DynamicLegoBannerViewHolder.LAYOUT
    }

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int {
        return RecommendationListCarouselViewHolder.LAYOUT
    }

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int {
        return ReminderWidgetViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OfficialBannerViewHolder.LAYOUT -> OfficialBannerViewHolder(parent)
            OfficialBenefitViewHolder.LAYOUT -> OfficialBenefitViewHolder(parent)
            OfficialFeaturedShopViewHolder.LAYOUT -> OfficialFeaturedShopViewHolder(parent, featuredShopListener)
            DynamicChannelThematicViewHolder.LAYOUT -> DynamicChannelThematicViewHolder(parent, dcEventHandler)
            DynamicChannelSprintSaleViewHolder.LAYOUT -> DynamicChannelSprintSaleViewHolder(parent, dcEventHandler)
            DynamicChannelMixLeftViewHolder.LAYOUT -> DynamicChannelMixLeftViewHolder(parent, dcEventHandler)
            DynamicChannelMixTopViewHolder.LAYOUT -> DynamicChannelMixTopViewHolder(parent, dcEventHandler)
            OfficialProductRecommendationTitleViewHolder.LAYOUT -> OfficialProductRecommendationTitleViewHolder(parent)
            OfficialProductRecommendationViewHolder.LAYOUT -> OfficialProductRecommendationViewHolder(parent, recommendationListener)
            OfficialLoadingContentViewHolder.LAYOUT -> OfficialLoadingContentViewHolder(parent)
            HideViewHolder.LAYOUT -> HideViewHolder(parent)
            DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(
                    parent, legoBannerListener, homeComponentListener
            )
            //deprecated - exist for remote config
            DynamicChannelLegoViewHolder.LAYOUT -> DynamicChannelLegoViewHolder(parent, dcEventHandler)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(mixLeftDataModel: MixLeftDataModel): Int {
        return MixLeftComponentViewHolder.LAYOUT
    }


    override fun type(mixTopDataModel: MixTopDataModel): Int {
        return MixTopComponentViewHolder.LAYOUT
    }

    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int {
        return ProductHighlightComponentViewHolder.LAYOUT
    }

    override fun type(lego4AutoDataModel: Lego4AutoDataModel): Int {
        return Lego4AutoBannerViewHolder.LAYOUT
    }

    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int {
        return FeaturedShopViewHolder.LAYOUT
    }
}
