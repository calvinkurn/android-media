package com.tokopedia.officialstore.official.presentation.adapter.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_component.listener.*
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.visitable.*
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.*
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel

class OfficialHomeAdapterTypeFactory(
        private val dcEventHandler: DynamicChannelEventHandler,
        private val featuredShopListener: FeaturedShopListener,
        private val recommendationWidgetListener: RecommendationWidgetListener,
        private val homeComponentListener: HomeComponentListener,
        private val legoBannerListener: DynamicLegoBannerListener,
        private val mixLeftComponentListener: MixLeftComponentListener,
        private val mixTopComponentListener: MixTopComponentListener,
        private val featuredShopDCListener: com.tokopedia.home_component.listener.FeaturedShopListener,
        private val recycledViewPool: RecyclerView.RecycledViewPool? = null,
        private val merchantVoucherComponentListener: MerchantVoucherComponentListener,
        private val specialReleaseComponentListener: SpecialReleaseComponentListener,
        private val onTopAdsHeadlineClicked: (applink: String) -> Unit,
        private val recommendationListener: RecommendationListener,
) : OfficialHomeTypeFactory, BaseAdapterTypeFactory(), RecommendationTypeFactory {

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

    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int {
        return FeaturedShopViewHolder.LAYOUT
    }

    override fun type(bannerDataModel: BannerDataModel): Int {
        return BannerComponentViewHolder.LAYOUT
    }

    override fun type(merchantVoucherDataModel: MerchantVoucherDataModel): Int {
        return MerchantVoucherViewHolder.LAYOUT
    }

    override fun type(bestSellerDataModel: BestSellerDataModel): Int {
        return BestSellerViewHolder.LAYOUT
    }

    override fun type(campaignWidgetDataModel: CampaignWidgetDataModel): Int = 0

    override fun type(officialTopAdsHeadlineDataModel: OfficialTopAdsHeadlineDataModel): Int {
        return OfficialTopAdsHeadlineViewHolder.LAYOUT
    }

    override fun type(specialReleaseDataModel: SpecialReleaseDataModel): Int {
        return SpecialReleaseViewHolder.LAYOUT
    }

    override fun type(officialTopAdsBannerDataModel: OfficialTopAdsBannerDataModel): Int {
        return OfficialTopAdsBannerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            MerchantVoucherViewHolder.LAYOUT -> MerchantVoucherViewHolder(view, merchantVoucherComponentListener)
            OfficialTopAdsHeadlineViewHolder.LAYOUT -> OfficialTopAdsHeadlineViewHolder(view, onTopAdsHeadlineClicked)
            BestSellerViewHolder.LAYOUT -> BestSellerViewHolder(view, recommendationWidgetListener)
            OfficialLoadingContentViewHolder.LAYOUT -> OfficialLoadingContentViewHolder(view)
            OfficialLoadingMoreViewHolder.LAYOUT -> OfficialLoadingMoreViewHolder(view)
            OfficialBannerViewHolder.LAYOUT -> OfficialBannerViewHolder(view, dcEventHandler)
            OfficialBenefitViewHolder.LAYOUT -> OfficialBenefitViewHolder(view)
            OfficialFeaturedShopViewHolder.LAYOUT -> OfficialFeaturedShopViewHolder(view, featuredShopListener)
            DynamicChannelSprintSaleViewHolder.LAYOUT -> DynamicChannelSprintSaleViewHolder(view, dcEventHandler)
            MixLeftComponentViewHolder.LAYOUT -> MixLeftComponentViewHolder(view, mixLeftComponentListener, homeComponentListener, recycledViewPool)
            MixTopComponentViewHolder.LAYOUT -> MixTopComponentViewHolder(view, homeComponentListener, mixTopComponentListener)
            OfficialProductRecommendationTitleViewHolder.LAYOUT -> OfficialProductRecommendationTitleViewHolder(view)
            OfficialProductRecommendationViewHolder.LAYOUT -> OfficialProductRecommendationViewHolder(view, recommendationListener)
            OfficialLoadingContentViewHolder.LAYOUT -> OfficialLoadingContentViewHolder(view)
            FeaturedShopViewHolder.LAYOUT -> FeaturedShopViewHolder(view, featuredShopDCListener, homeComponentListener)
            HideViewHolder.LAYOUT -> HideViewHolder(view)
            DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(view, legoBannerListener, homeComponentListener)
            OfficialTopAdsBannerViewHolder.LAYOUT -> OfficialTopAdsBannerViewHolder(view)
            EmptyBlankViewHolder.LAYOUT -> EmptyBlankViewHolder(view)
            SpecialReleaseViewHolder.LAYOUT -> SpecialReleaseViewHolder(
                view,
                homeComponentListener,
                specialReleaseComponentListener
            )
            else -> super.createViewHolder(view, type)
        }  as AbstractViewHolder<Visitable<*>>
    }
}
