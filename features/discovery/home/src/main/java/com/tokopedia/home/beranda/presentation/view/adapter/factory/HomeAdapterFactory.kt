package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeFeedsListener
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CMHomeWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelLoadingModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.EmptyBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateAtfModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelOneModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelThreeModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelTwoModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateIconModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeLoadingMoreModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomePayLaterWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsVerticalBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.InspirationHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ShimmeringChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ShimmeringIconDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.UseCaseIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.HomeInitialShimmerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.InspirationHeaderViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.RetryViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.UseCaseIconSectionViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.BannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.CMHomeWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.CarouselPlayWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.CategoryWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelSprintViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicIconSectionViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicIconTwoRowsSectionViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.EmptyBannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.HomeHeaderAtf2ViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.HomeHeaderOvoViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.HomeLoadingMoreViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.HomePayLaterWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PlayCardViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.ProductOrganicChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.ReviewViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.SpotlightViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.TickerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.TopAdsVerticalBannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.TopadsBannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.default_home_dc.ErrorPromptViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.NewBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.DynamicChannelLoadingViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.DynamicChannelRetryViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.EmptyBlankViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ErrorStateChannelOneViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ErrorStateChannelThreeViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ErrorStateChannelTwoViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ErrorStateIconViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.HomeAtfErrorViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ShimmeringChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ShimmeringIconViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.beranda.presentation.view.listener.CMHomeWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.CarouselPlayWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.HomePayLaterWidgetListener
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeInitialShimmerDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.CampaignWidgetComponentListener
import com.tokopedia.home_component.listener.CategoryNavigationListener
import com.tokopedia.home_component.listener.CategoryWidgetV2Listener
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.listener.DynamicIconComponentListener
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.listener.FlashSaleWidgetListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.listener.Lego6AutoBannerListener
import com.tokopedia.home_component.listener.LegoProductListener
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.listener.ProductHighlightListener
import com.tokopedia.home_component.listener.RecommendationListCarouselListener
import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.listener.SpecialReleaseComponentListener
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.BannerRevampViewHolder
import com.tokopedia.home_component.viewholders.CampaignWidgetViewHolder
import com.tokopedia.home_component.viewholders.CategoryNavigationViewHolder
import com.tokopedia.home_component.viewholders.CategoryWidgetV2ViewHolder
import com.tokopedia.home_component.viewholders.CueWidgetCategoryViewHolder
import com.tokopedia.home_component.viewholders.DealsWidgetViewHolder
import com.tokopedia.home_component.viewholders.DynamicIconViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerSixAutoViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.FlashSaleViewHolder
import com.tokopedia.home_component.viewholders.Lego4AutoBannerViewHolder
import com.tokopedia.home_component.viewholders.Lego4ProductViewHolder
import com.tokopedia.home_component.viewholders.MerchantVoucherViewHolder
import com.tokopedia.home_component.viewholders.MissionWidgetViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixLeftPaddingComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.viewholders.ProductHighlightComponentViewHolder
import com.tokopedia.home_component.viewholders.QuestWidgetViewHolder
import com.tokopedia.home_component.viewholders.RecommendationListCarouselViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.home_component.viewholders.SpecialReleaseViewHolder
import com.tokopedia.home_component.viewholders.TodoWidgetViewHolder
import com.tokopedia.home_component.viewholders.VpsWidgetViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.home_component.visitable.CampaignWidgetDataModel
import com.tokopedia.home_component.visitable.CategoryNavigationDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.CueCategoryDataModel
import com.tokopedia.home_component.visitable.DealsDataModel
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.FlashSaleDataModel
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.Lego4ProductDataModel
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixLeftPaddingDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.visitable.VpsDataModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.recharge_component.RechargeComponentTypeFactory
import com.tokopedia.recharge_component.listener.RechargeBUWidgetListener
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetMixLeftViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetMixTopViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel

/**
 * @author by errysuprayogi on 11/28/17.
 */

class HomeAdapterFactory(
    private val listener: HomeCategoryListener,
    private val homeFeedsListener: HomeFeedsListener,
    private val homeReviewListener: HomeReviewListener,
    private val parentRecycledViewPool: RecyclerView.RecycledViewPool,
    private val popularKeywordListener: PopularKeywordViewHolder.PopularKeywordListener,
    private val homeComponentListener: HomeComponentListener,
    private val legoListener: DynamicLegoBannerListener,
    private val recommendationListCarouselListener: RecommendationListCarouselListener,
    private val mixLeftComponentListener: MixLeftComponentListener,
    private val mixTopComponentListener: MixTopComponentListener,
    private val reminderWidgetListener: ReminderWidgetListener,
    private val productHighlightListener: ProductHighlightListener,
    private val lego4AutoBannerListener: Lego4AutoBannerListener,
    private val featuredShopListener: FeaturedShopListener,
    private val playWidgetCoordinator: PlayWidgetCoordinator,
    private val bestSellerListener: RecommendationWidgetListener,
    private val categoryNavigationListener: CategoryNavigationListener,
    private val rechargeBUWidgetListener: RechargeBUWidgetListener,
    private val bannerComponentListener: BannerComponentListener?,
    private val dynamicIconComponentListener: DynamicIconComponentListener,
    private val legoSixAutoListener: Lego6AutoBannerListener,
    private val campaignWidgetComponentListener: CampaignWidgetComponentListener,
    private val questWidgetCallbacks: QuestWidgetCallbacks,
    private val cmHomeWidgetCallback: CMHomeWidgetCallback,
    private val homePayLaterWidgetListener: HomePayLaterWidgetListener,
    private val specialReleaseComponentListener: SpecialReleaseComponentListener,
    private val merchantVoucherComponentListener: MerchantVoucherComponentListener,
    private val cueWidgetCategoryListener: CueWidgetCategoryListener,
    private val vpsWidgetListener: VpsWidgetListener,
    private val categoryWidgetV2Listener: CategoryWidgetV2Listener,
    private val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val legoProductListener: LegoProductListener,
    private val todoWidgetComponentListener: TodoWidgetComponentListener,
    private val flashSaleWidgetListener: FlashSaleWidgetListener,
    private val carouselPlayWidgetCallback: CarouselPlayWidgetCallback,
) : BaseAdapterTypeFactory(),
    HomeTypeFactory,
    HomeComponentTypeFactory,
    RecommendationTypeFactory,
    RechargeComponentTypeFactory {

    private val productLayout = HashSet(
        listOf(
            DynamicHomeChannel.Channels.LAYOUT_3_IMAGE,
            DynamicHomeChannel.Channels.LAYOUT_SPRINT
        )
    )

    private val bannerLayout = HashSet(
        listOf(
            DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL,
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC
        )
    )

    override fun type(inspirationHeaderDataModel: InspirationHeaderDataModel): Int {
        return InspirationHeaderViewHolder.LAYOUT
    }

    override fun type(homepageBannerDataModel: HomepageBannerDataModel): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(bestSellerDataModel: BestSellerDataModel): Int {
        return BestSellerViewHolder.LAYOUT
    }

    override fun type(tickerDataModel: TickerDataModel): Int {
        return TickerViewHolder.LAYOUT
    }

    override fun type(businessUnitWidgetDataModel: NewBusinessUnitWidgetDataModel): Int {
        return NewBusinessViewHolder.LAYOUT
    }

    override fun type(useCaseIconSectionDataModel: UseCaseIconSectionDataModel): Int {
        return UseCaseIconSectionViewHolder.LAYOUT
    }

    override fun type(dynamicIconSectionDataModel: DynamicIconSectionDataModel): Int {
        return if (dynamicIconSectionDataModel.dynamicIconWrap) DynamicIconTwoRowsSectionViewHolder.LAYOUT else DynamicIconSectionViewHolder.LAYOUT
    }

    override fun type(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel): Int {
        return HomeRecommendationFeedViewHolder.LAYOUT
    }

    override fun type(dynamicChannelDataModel: DynamicChannelDataModel): Int {
        val layout = dynamicChannelDataModel.channel?.layout ?: ""
        return getDynamicChannelLayoutFromType(layout)
    }

    override fun type(spotlightDataModel: SpotlightDataModel): Int {
        return SpotlightViewHolder.LAYOUT
    }

    fun type(retryModel: RetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    override fun type(reviewDataModel: ReviewDataModel): Int {
        return ReviewViewHolder.LAYOUT
    }

    override fun type(playCard: PlayCardDataModel): Int {
        return PlayCardViewHolder.LAYOUT
    }

    override fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int {
        return HomeLoadingMoreViewHolder.LAYOUT
    }

    override fun type(homeRetryModel: HomeRetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    override fun type(popularKeywordListDataModel: PopularKeywordListDataModel): Int {
        return PopularKeywordViewHolder.LAYOUT
    }

    override fun type(homeTopAdsBannerDataModel: HomeTopAdsBannerDataModel): Int {
        return TopadsBannerViewHolder.LAYOUT
    }
    override fun type(homeTopAdsVerticalBannerDataModel: HomeTopAdsVerticalBannerDataModel): Int {
        return TopAdsVerticalBannerViewHolder.LAYOUT
    }

    // Home-Component
    override fun type(categoryWidgetV2DataModel: CategoryWidgetV2DataModel): Int {
        return CategoryWidgetV2ViewHolder.LAYOUT
    }

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int {
        return DynamicLegoBannerViewHolder.LAYOUT
    }

    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int {
        return DynamicLegoBannerSixAutoViewHolder.LAYOUT
    }

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int {
        return RecommendationListCarouselViewHolder.LAYOUT
    }

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int {
        return ReminderWidgetViewHolder.LAYOUT
    }

    override fun type(rechargeBUWidgetDataModel: RechargeBUWidgetDataModel): Int {
        return if (rechargeBUWidgetDataModel.data.option1 == RechargeBUWidgetMixTopViewHolder.BU_WIDGET_TYPE_TOP) {
            RechargeBUWidgetMixTopViewHolder.LAYOUT
        } else {
            RechargeBUWidgetMixLeftViewHolder.LAYOUT
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

    override fun type(dynamicChannelLoadingModel: DynamicChannelLoadingModel): Int {
        return DynamicChannelLoadingViewHolder.LAYOUT
    }

    override fun type(dynamicChannelRetryModel: DynamicChannelRetryModel): Int {
        return DynamicChannelRetryViewHolder.LAYOUT
    }

    override fun type(lego4AutoDataModel: Lego4AutoDataModel): Int {
        return Lego4AutoBannerViewHolder.LAYOUT
    }

    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int {
        return FeaturedShopViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselPlayWidgetDataModel) =
        CarouselPlayWidgetViewHolder.LAYOUT

    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int {
        return CategoryNavigationViewHolder.LAYOUT
    }

    override fun type(bannerDataModel: BannerDataModel): Int {
        return BannerComponentViewHolder.LAYOUT
    }

    // end of Home-Component section

    override fun type(emptyBannerDataModel: EmptyBannerDataModel): Int {
        return EmptyBannerViewHolder.LAYOUT
    }

    override fun type(homeHeaderOvoDataModel: HomeHeaderDataModel): Int {
        return if (HomeRollenceController.isUsingAtf2Variant()) {
            HomeHeaderAtf2ViewHolder.LAYOUT
        } else {
            HomeHeaderOvoViewHolder.LAYOUT
        }
    }

    override fun type(homeInitialShimmerDataModel: HomeInitialShimmerDataModel): Int {
        return HomeInitialShimmerViewHolder.LAYOUT
    }

    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int {
        return DynamicIconViewHolder.LAYOUT
    }

    override fun type(errorStateIconModel: ErrorStateIconModel): Int {
        return ErrorStateIconViewHolder.LAYOUT
    }

    override fun type(errorStateChannelOneModel: ErrorStateChannelOneModel): Int {
        return ErrorStateChannelOneViewHolder.LAYOUT
    }

    override fun type(errorStateChannelTwoModel: ErrorStateChannelTwoModel): Int {
        return ErrorStateChannelTwoViewHolder.LAYOUT
    }

    override fun type(errorStateChannelThreeModel: ErrorStateChannelThreeModel): Int {
        return ErrorStateChannelThreeViewHolder.LAYOUT
    }

    override fun type(shimmeringChannelDataModel: ShimmeringChannelDataModel): Int {
        return ShimmeringChannelViewHolder.LAYOUT
    }

    override fun type(shimmeringIconDataModel: ShimmeringIconDataModel): Int {
        return ShimmeringIconViewHolder.LAYOUT
    }

    override fun type(errorStateAtfModel: ErrorStateAtfModel): Int {
        return HomeAtfErrorViewHolder.LAYOUT
    }

    override fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int {
        return 0
    }

    override fun type(questWidgetModel: QuestWidgetModel): Int {
        return QuestWidgetViewHolder.LAYOUT
    }

    override fun type(campaignWidgetDataModel: CampaignWidgetDataModel): Int {
        return CampaignWidgetViewHolder.LAYOUT
    }

    override fun type(merchantVoucherDataModel: MerchantVoucherDataModel): Int {
        return MerchantVoucherViewHolder.LAYOUT
    }

    override fun type(specialReleaseDataModel: SpecialReleaseDataModel): Int {
        return SpecialReleaseViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetDataModel: CMHomeWidgetDataModel): Int {
        return CMHomeWidgetViewHolder.LAYOUT
    }

    override fun type(homePayLaterWidgetDataModel: HomePayLaterWidgetDataModel): Int {
        return HomePayLaterWidgetViewHolder.LAYOUT
    }

    override fun type(cueCategoryDataModel: CueCategoryDataModel): Int {
        return CueWidgetCategoryViewHolder.LAYOUT
    }

    override fun type(vpsDataModel: VpsDataModel): Int {
        return VpsWidgetViewHolder.LAYOUT
    }

    override fun type(missionWidgetListDataModel: MissionWidgetListDataModel): Int {
        return MissionWidgetViewHolder.LAYOUT
    }

    override fun type(lego4ProductDataModel: Lego4ProductDataModel): Int {
        return Lego4ProductViewHolder.LAYOUT
    }

    override fun type(mixLeftPaddingDataModel: MixLeftPaddingDataModel): Int {
        return MixLeftPaddingComponentViewHolder.LAYOUT
    }

    override fun type(bannerRevampDataModel: BannerRevampDataModel): Int {
        return BannerRevampViewHolder.LAYOUT
    }

    override fun type(todoWidgetListDataModel: TodoWidgetListDataModel): Int {
        return TodoWidgetViewHolder.LAYOUT
    }

    override fun type(dealsDataModel: DealsDataModel): Int {
        return DealsWidgetViewHolder.LAYOUT
    }

    override fun type(flashSaleDataModel: FlashSaleDataModel): Int {
        return FlashSaleViewHolder.LAYOUT
    }

    private fun getDynamicChannelLayoutFromType(layout: String): Int {
        /**
         * Layout registered as sprint sale viewholder
         * refer to item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
         */
        if (productLayout.contains(layout)) {
            return ProductOrganicChannelViewHolder.LAYOUT
        } else if (DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO.contains(layout)) {
            /**
             * Layout registered as sprint sale
             * refer to dynamic channel sprint layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item
             */
            return DynamicChannelSprintViewHolder.LAYOUT
        }

        return when (layout) {
            /**
             * refer to 1 grid item layout {@link com.tokopedia.home.R.layout#home_dc_category_widget}
             * used by category widget
             */
            DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET -> CategoryWidgetViewHolder.LAYOUT

            /**
             * refer to 1 grid item layout {@link com.tokopedia.home.R.layout#home_dc_category_widget_v2}
             * used by category widget v2
             */
            DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET_V2 -> CategoryWidgetV2ViewHolder.LAYOUT

            /**
             * refer to popular keyword layout com.tokopedia.home.R.layout#layout_popular_image
             */
            DynamicHomeChannel.Channels.LAYOUT_POPULAR_KEYWORD -> PopularKeywordViewHolder.LAYOUT

            /**
             * refer to gif banner layout com.tokopedia.home.R.layout#home_dc_default_error_prompt
             */
            DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR -> ErrorPromptViewHolder.LAYOUT

            /**
             * refer to mix top carousel com.tokopedia.home.R.layout#home_mix_top_banner
             */
            DynamicHomeChannel.Channels.LAYOUT_BANNER_ADS -> TopadsBannerViewHolder.LAYOUT

            DynamicHomeChannel.Channels.LAYOUT_VERTICAL_BANNER_ADS -> TopAdsVerticalBannerViewHolder.LAYOUT

            else -> EmptyBlankViewHolder.LAYOUT
        }
    }
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            EmptyBannerViewHolder.LAYOUT -> viewHolder = EmptyBannerViewHolder(view, listener)
            HomeHeaderOvoViewHolder.LAYOUT -> viewHolder = HomeHeaderOvoViewHolder(view, listener)
            HomeHeaderAtf2ViewHolder.LAYOUT -> viewHolder = HomeHeaderAtf2ViewHolder(view, listener)
            HomeInitialShimmerViewHolder.LAYOUT -> viewHolder = HomeInitialShimmerViewHolder(view, listener)
            DynamicChannelSprintViewHolder.LAYOUT -> viewHolder = DynamicChannelSprintViewHolder(view, listener, parentRecycledViewPool)
            ProductOrganicChannelViewHolder.LAYOUT -> viewHolder = ProductOrganicChannelViewHolder(view, listener, parentRecycledViewPool)
            BannerViewHolder.LAYOUT -> viewHolder = BannerViewHolder(view, listener)
            TickerViewHolder.LAYOUT -> viewHolder = TickerViewHolder(view, listener)
            NewBusinessViewHolder.LAYOUT -> viewHolder = NewBusinessViewHolder(view, listener, cardInteraction = true)
            UseCaseIconSectionViewHolder.LAYOUT -> viewHolder = UseCaseIconSectionViewHolder(view, listener)
            DynamicIconSectionViewHolder.LAYOUT -> viewHolder = DynamicIconSectionViewHolder(view, listener)
            DynamicIconTwoRowsSectionViewHolder.LAYOUT -> viewHolder = DynamicIconTwoRowsSectionViewHolder(view, listener)
            RetryViewHolder.LAYOUT -> viewHolder = RetryViewHolder(view, homeFeedsListener)
            SpotlightViewHolder.LAYOUT -> viewHolder = SpotlightViewHolder(view, listener)
            EmptyBlankViewHolder.LAYOUT -> viewHolder = EmptyBlankViewHolder(view)
            InspirationHeaderViewHolder.LAYOUT -> viewHolder = InspirationHeaderViewHolder(view)
            HomeRecommendationFeedViewHolder.LAYOUT -> viewHolder = HomeRecommendationFeedViewHolder(view, listener, cardInteraction = true)
            ReviewViewHolder.LAYOUT -> viewHolder = ReviewViewHolder(view, homeReviewListener, listener, cardInteraction = true)
            PlayCardViewHolder.LAYOUT -> viewHolder = PlayCardViewHolder(view, listener)
            HomeLoadingMoreViewHolder.LAYOUT -> viewHolder = HomeLoadingMoreViewHolder(view)
            ErrorPromptViewHolder.LAYOUT -> viewHolder = ErrorPromptViewHolder(view, listener)
            PopularKeywordViewHolder.LAYOUT -> viewHolder = PopularKeywordViewHolder(view, listener, popularKeywordListener, cardInteraction = true)
            CategoryWidgetViewHolder.LAYOUT -> viewHolder = CategoryWidgetViewHolder(view, listener)
            CategoryWidgetV2ViewHolder.LAYOUT -> viewHolder = CategoryWidgetV2ViewHolder(view, categoryWidgetV2Listener, cardInteraction = true)
            BestSellerViewHolder.LAYOUT -> viewHolder = BestSellerViewHolder(view, bestSellerListener, cardInteraction = true)
            ProductHighlightComponentViewHolder.LAYOUT -> viewHolder = ProductHighlightComponentViewHolder(
                view,
                homeComponentListener,
                productHighlightListener,
                cardInteraction = true
            )
            DynamicLegoBannerViewHolder.LAYOUT ->
                viewHolder =
                    DynamicLegoBannerViewHolder(
                        view,
                        legoListener,
                        homeComponentListener,
                        parentRecycledViewPool
                    )
            RecommendationListCarouselViewHolder.LAYOUT ->
                viewHolder =
                    RecommendationListCarouselViewHolder(
                        view,
                        recommendationListCarouselListener,
                        parentRecycledViewPool,
                        cardInteraction = true
                    )
            MixLeftComponentViewHolder.LAYOUT ->
                viewHolder =
                    MixLeftComponentViewHolder(
                        view,
                        mixLeftComponentListener,
                        homeComponentListener,
                        parentRecycledViewPool,
                        cardInteraction = true
                    )
            MixTopComponentViewHolder.LAYOUT ->
                viewHolder =
                    MixTopComponentViewHolder(
                        view,
                        homeComponentListener,
                        mixTopComponentListener,
                        cardInteraction = true
                    )
            ReminderWidgetViewHolder.LAYOUT ->
                viewHolder =
                    ReminderWidgetViewHolder(view, reminderWidgetListener, cardInteraction = true)
            TopadsBannerViewHolder.LAYOUT -> viewHolder = TopadsBannerViewHolder(view, listener)
            TopAdsVerticalBannerViewHolder.LAYOUT -> viewHolder = TopAdsVerticalBannerViewHolder(view)
            DynamicChannelLoadingViewHolder.LAYOUT -> viewHolder = DynamicChannelLoadingViewHolder(view)
            DynamicChannelRetryViewHolder.LAYOUT -> viewHolder = DynamicChannelRetryViewHolder(view, listener)
            Lego4AutoBannerViewHolder.LAYOUT ->
                viewHolder =
                    Lego4AutoBannerViewHolder(
                        view,
                        lego4AutoBannerListener,
                        homeComponentListener,
                        parentRecycledViewPool
                    )
            FeaturedShopViewHolder.LAYOUT -> viewHolder = FeaturedShopViewHolder(
                view,
                featuredShopListener,
                homeComponentListener,
                cardInteraction = true
            )
            CategoryNavigationViewHolder.LAYOUT -> viewHolder = CategoryNavigationViewHolder(view, categoryNavigationListener)
            CarouselPlayWidgetViewHolder.LAYOUT -> {
                val playWidgetView = view.findViewById<View>(R.id.playWidgetView)
                viewHolder = if (playWidgetView != null) {
                    CarouselPlayWidgetViewHolder(view, PlayWidgetViewHolder(playWidgetView, playWidgetCoordinator), carouselPlayWidgetCallback, listener)
                } else {
                    super.createViewHolder(view, type)
                }
            }
            RechargeBUWidgetMixLeftViewHolder.LAYOUT ->
                viewHolder =
                    RechargeBUWidgetMixLeftViewHolder(view, rechargeBUWidgetListener)
            RechargeBUWidgetMixTopViewHolder.LAYOUT ->
                viewHolder =
                    RechargeBUWidgetMixTopViewHolder(view, rechargeBUWidgetListener)
            BannerComponentViewHolder.LAYOUT ->
                viewHolder =
                    BannerComponentViewHolder(view, bannerComponentListener, homeComponentListener)
            DynamicIconViewHolder.LAYOUT ->
                viewHolder =
                    DynamicIconViewHolder(view, dynamicIconComponentListener, HomeRollenceController.isUsingAtf2Variant())
            ErrorStateIconViewHolder.LAYOUT -> viewHolder = ErrorStateIconViewHolder(view, listener)
            ErrorStateChannelOneViewHolder.LAYOUT ->
                viewHolder =
                    ErrorStateChannelOneViewHolder(view, listener)
            ErrorStateChannelTwoViewHolder.LAYOUT ->
                viewHolder =
                    ErrorStateChannelTwoViewHolder(view, listener)
            ErrorStateChannelThreeViewHolder.LAYOUT ->
                viewHolder =
                    ErrorStateChannelThreeViewHolder(view, listener)
            HomeInitialShimmerViewHolder.LAYOUT ->
                viewHolder =
                    HomeInitialShimmerViewHolder(view, listener)
            ShimmeringChannelViewHolder.LAYOUT ->
                viewHolder =
                    ShimmeringChannelViewHolder(view, listener)
            ShimmeringIconViewHolder.LAYOUT -> viewHolder = ShimmeringIconViewHolder(view, listener)
            HomeAtfErrorViewHolder.LAYOUT -> viewHolder = HomeAtfErrorViewHolder(view, listener)
            DynamicLegoBannerSixAutoViewHolder.LAYOUT ->
                viewHolder =
                    DynamicLegoBannerSixAutoViewHolder(
                        view,
                        legoSixAutoListener,
                        homeComponentListener,
                        parentRecycledViewPool
                    )
            QuestWidgetViewHolder.LAYOUT ->
                viewHolder =
                    QuestWidgetViewHolder(view, questWidgetCallbacks)
            CampaignWidgetViewHolder.LAYOUT -> viewHolder = CampaignWidgetViewHolder(
                view,
                homeComponentListener,
                campaignWidgetComponentListener,
                parentRecycledViewPool,
                cardInteraction = true
            )
            MerchantVoucherViewHolder.LAYOUT ->
                viewHolder =
                    MerchantVoucherViewHolder(view, merchantVoucherComponentListener, cardInteraction = true)
            SpecialReleaseViewHolder.LAYOUT -> viewHolder = SpecialReleaseViewHolder(
                view,
                homeComponentListener,
                specialReleaseComponentListener,
                cardInteraction = true
            )
            CMHomeWidgetViewHolder.LAYOUT -> viewHolder = CMHomeWidgetViewHolder(
                view,
                cmHomeWidgetCallback
            )
            HomePayLaterWidgetViewHolder.LAYOUT -> viewHolder = HomePayLaterWidgetViewHolder(
                view,
                homePayLaterWidgetListener
            )
            CueWidgetCategoryViewHolder.LAYOUT -> viewHolder = CueWidgetCategoryViewHolder(view, cueWidgetCategoryListener)
            VpsWidgetViewHolder.LAYOUT -> viewHolder = VpsWidgetViewHolder(view, vpsWidgetListener, homeComponentListener, parentRecycledViewPool)
            MissionWidgetViewHolder.LAYOUT -> viewHolder = MissionWidgetViewHolder(view, missionWidgetComponentListener, cardInteraction = true)
            Lego4ProductViewHolder.LAYOUT -> viewHolder = Lego4ProductViewHolder(view, legoProductListener, homeComponentListener, parentRecycledViewPool, cardInteraction = true)
            MixLeftPaddingComponentViewHolder.LAYOUT ->
                viewHolder =
                    MixLeftPaddingComponentViewHolder(
                        view,
                        mixLeftComponentListener,
                        homeComponentListener,
                        cardInteraction = true
                    )
            TodoWidgetViewHolder.LAYOUT ->
                viewHolder =
                    TodoWidgetViewHolder(
                        view,
                        todoWidgetComponentListener
                    )
            BannerRevampViewHolder.LAYOUT ->
                viewHolder =
                    BannerRevampViewHolder(view, bannerComponentListener, cardInteraction = true)
            MixLeftPaddingComponentViewHolder.LAYOUT ->
                viewHolder =
                    MixLeftPaddingComponentViewHolder(
                        view,
                        mixLeftComponentListener,
                        homeComponentListener,
                        cardInteraction = true
                    )
            DealsWidgetViewHolder.LAYOUT ->
                viewHolder = DealsWidgetViewHolder(view, vpsWidgetListener, homeComponentListener, parentRecycledViewPool)
            FlashSaleViewHolder.LAYOUT -> viewHolder = FlashSaleViewHolder(view, flashSaleWidgetListener, homeComponentListener, parentRecycledViewPool)
            else -> viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder
    }
}
