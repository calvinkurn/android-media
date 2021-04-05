package com.tokopedia.officialstore.official.presentation.adapter.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.visitable.*
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.*
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.*

class OfficialHomeAdapterTypeFactory(
        private val dcEventHandler: DynamicChannelEventHandler,
        private val featuredShopListener: FeaturedShopListener,
        private val homeComponentListener: HomeComponentListener,
        private val legoBannerListener: DynamicLegoBannerListener,
        private val mixLeftComponentListener: MixLeftComponentListener,
        private val mixTopComponentListener: MixTopComponentListener,
        private val recycledViewPool: RecyclerView.RecycledViewPool? = null
) : OfficialHomeTypeFactory, BaseAdapterTypeFactory() {

    override fun type(officialLoadingDataModel: OfficialLoadingDataModel): Int {
        return OfficialLoadingContentViewHolder.LAYOUT
    }

    override fun type(officialLoadingMoreDataModel: OfficialLoadingMoreDataModel): Int {
        return OfficialLoadingMoreViewHolder.LAYOUT
    }

    override fun type(officialBannerDataModel: OfficialBannerDataModel): Int {
        return if (officialBannerDataModel.banner.isEmpty())
            HideViewHolder.LAYOUT
        else
            OfficialBannerViewHolder.LAYOUT
    }
    override fun type(officialBenefitDataModel: OfficialBenefitDataModel): Int {
        return if (officialBenefitDataModel.benefit.isEmpty())
            HideViewHolder.LAYOUT
        else
            OfficialBenefitViewHolder.LAYOUT
    }

    override fun type(officialFeaturedShopDataModel: OfficialFeaturedShopDataModel): Int {
        return if (officialFeaturedShopDataModel.featuredShop.isEmpty())
            HideViewHolder.LAYOUT
        else
            OfficialFeaturedShopViewHolder.LAYOUT
    }

    override fun type(dynamicChannelDataModel: DynamicChannelDataModel): Int {
        return dynamicChannelDataModel.getLayoutType()
    }

    override fun type(productRecommendationTitleDataModel: ProductRecommendationTitleDataModel): Int {
        return OfficialProductRecommendationTitleViewHolder.LAYOUT
    }

    override fun type(productRecommendationDataModel: ProductRecommendationDataModel): Int {
        return OfficialProductRecommendationViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int = OfficialLoadingContentViewHolder.LAYOUT

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int {
        return DynamicLegoBannerViewHolder.LAYOUT
    }

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int {
        return RecommendationListCarouselViewHolder.LAYOUT
    }

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int {
        return ReminderWidgetViewHolder.LAYOUT
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

    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int {
        return 0
    }

    override fun type(bannerDataModel: BannerDataModel): Int {
        return BannerComponentViewHolder.LAYOUT
    }

    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int {
        return -1
    }

    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int {
        return -1
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            OfficialLoadingContentViewHolder.LAYOUT -> OfficialLoadingContentViewHolder(view)
            OfficialLoadingMoreViewHolder.LAYOUT -> OfficialLoadingMoreViewHolder(view)
            OfficialBannerViewHolder.LAYOUT -> OfficialBannerViewHolder(view)
            OfficialBenefitViewHolder.LAYOUT -> OfficialBenefitViewHolder(view)
            OfficialFeaturedShopViewHolder.LAYOUT -> OfficialFeaturedShopViewHolder(view, featuredShopListener)
            DynamicChannelThematicViewHolder.LAYOUT -> DynamicChannelThematicViewHolder(view, dcEventHandler)
            DynamicChannelSprintSaleViewHolder.LAYOUT -> DynamicChannelSprintSaleViewHolder(view, dcEventHandler)
            MixLeftComponentViewHolder.LAYOUT -> MixLeftComponentViewHolder(
                    view,
                    mixLeftComponentListener,
                    homeComponentListener,
                    recycledViewPool
            )
            MixTopComponentViewHolder.LAYOUT -> MixTopComponentViewHolder(view, homeComponentListener, mixTopComponentListener)
            OfficialProductRecommendationTitleViewHolder.LAYOUT -> OfficialProductRecommendationTitleViewHolder(view)
            OfficialProductRecommendationViewHolder.LAYOUT -> OfficialProductRecommendationViewHolder(view)
            OfficialLoadingContentViewHolder.LAYOUT -> OfficialLoadingContentViewHolder(view)
            HideViewHolder.LAYOUT -> HideViewHolder(view)
            DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(
                    view, legoBannerListener, homeComponentListener
            )
            //deprecated - exist for remote config
            DynamicChannelLegoViewHolder.LAYOUT -> DynamicChannelLegoViewHolder(view, dcEventHandler)
            else -> super.createViewHolder(view, type)
        }  as AbstractViewHolder<Visitable<*>>
    }
}
