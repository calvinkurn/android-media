package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeFeedsListener
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.default_home_dc.ErrorPromptViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.NewBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeInitialShimmerDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.*
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.visitable.*
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.recharge_component.RechargeComponentTypeFactory
import com.tokopedia.recharge_component.listener.RechargeBUWidgetListener
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetMixLeftViewHolder
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetMixTopViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import java.util.*

/**
 * @author by errysuprayogi on 11/28/17.
 */

class HomeAdapterFactory(private val listener: HomeCategoryListener, private val homeFeedsListener: HomeFeedsListener,
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
                         private val legoSixAutoListener: Lego6AutoBannerListener
) :
        BaseAdapterTypeFactory(),
        HomeTypeFactory, HomeComponentTypeFactory, RecommendationTypeFactory,
        RechargeComponentTypeFactory {

    private val productLayout = HashSet(
            listOf(
                    DynamicHomeChannel.Channels.LAYOUT_3_IMAGE,
                    DynamicHomeChannel.Channels.LAYOUT_SPRINT,
                    DynamicHomeChannel.Channels.LAYOUT_ORGANIC)
    )

    private val bannerLayout = HashSet(
            listOf(
                    DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL,
                    DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC)
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
        return if(dynamicIconSectionDataModel.dynamicIconWrap) DynamicIconTwoRowsSectionViewHolder.LAYOUT else DynamicIconSectionViewHolder.LAYOUT
    }

    override fun type(sellDataModel: SellDataModel): Int {
        return SellViewHolder.LAYOUT
    }

    override fun type(headerDataModel: HeaderDataModel): Int {
        return if (headerDataModel.isUserLogin)
            OvoViewHolder.LAYOUT
        else OvoViewHolder.NON_LOGIN_LAYOUT
    }

    override fun type(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel): Int {
        return HomeRecommendationFeedViewHolder.LAYOUT
    }

    override fun type(dynamicChannelDataModel: DynamicChannelDataModel): Int {
        val layout = dynamicChannelDataModel.channel?.layout?:""
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

    //Home-Component
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
        return if (rechargeBUWidgetDataModel.data.option1 == RechargeBUWidgetMixTopViewHolder.BU_WIDGET_TYPE_TOP)
        RechargeBUWidgetMixTopViewHolder.LAYOUT else RechargeBUWidgetMixLeftViewHolder.LAYOUT
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

    //end of Home-Component section

    override fun type(emptyBannerDataModel: EmptyBannerDataModel): Int {
        return EmptyBannerViewHolder.LAYOUT
    }

    override fun type(homeHeaderOvoDataModel: HomeHeaderOvoDataModel): Int {
        return HomeHeaderOvoViewHolder.LAYOUT
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

    private fun getDynamicChannelLayoutFromType(layout: String): Int {
        /**
         * Layout registered as sprint sale viewholder
         * refer to item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
         */
        if (productLayout.contains(layout)) {
            return ProductOrganicChannelViewHolder.LAYOUT
        }
        else if(DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO.contains(layout)){
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
             * refer to sprint product item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
             * no further development for this viewholder
             * backend possibly return this layout for android version  >= 2.19
             */
            DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL -> SprintSaleCarouselViewHolder.LAYOUT

            /**
             * refer to gif banner layout com.tokopedia.home.R.layout#banner_image
             */
            DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> BannerImageViewHolder.LAYOUT

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
            else -> EmptyBlankViewHolder.LAYOUT
        }
    }
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            EmptyBannerViewHolder.LAYOUT -> viewHolder = EmptyBannerViewHolder(view, listener)
            HomeHeaderOvoViewHolder.LAYOUT -> viewHolder = HomeHeaderOvoViewHolder(view, listener)
            HomeInitialShimmerViewHolder.LAYOUT -> viewHolder = HomeInitialShimmerViewHolder(view, listener)
            DynamicChannelSprintViewHolder.LAYOUT -> viewHolder = DynamicChannelSprintViewHolder(view, listener, parentRecycledViewPool)
            ProductOrganicChannelViewHolder.LAYOUT -> viewHolder = ProductOrganicChannelViewHolder(view, listener, parentRecycledViewPool)
            BannerViewHolder.LAYOUT -> viewHolder = BannerViewHolder(view, listener)
            TickerViewHolder.LAYOUT -> viewHolder = TickerViewHolder(view, listener)
            NewBusinessViewHolder.LAYOUT -> viewHolder = NewBusinessViewHolder(view, listener)
            UseCaseIconSectionViewHolder.LAYOUT -> viewHolder = UseCaseIconSectionViewHolder(view, listener)
            DynamicIconSectionViewHolder.LAYOUT -> viewHolder = DynamicIconSectionViewHolder(view, listener)
            DynamicIconTwoRowsSectionViewHolder.LAYOUT -> viewHolder = DynamicIconTwoRowsSectionViewHolder(view, listener)
            SellViewHolder.LAYOUT -> viewHolder = SellViewHolder(view, listener)
            OvoViewHolder.LAYOUT, OvoViewHolder.NON_LOGIN_LAYOUT -> viewHolder = OvoViewHolder(view, listener)
            RetryViewHolder.LAYOUT -> viewHolder = RetryViewHolder(view, homeFeedsListener)
            SprintSaleCarouselViewHolder.LAYOUT -> viewHolder = SprintSaleCarouselViewHolder(view, listener)
            SpotlightViewHolder.LAYOUT -> viewHolder = SpotlightViewHolder(view, listener)
            EmptyBlankViewHolder.LAYOUT -> viewHolder = EmptyBlankViewHolder(view)
            InspirationHeaderViewHolder.LAYOUT -> viewHolder = InspirationHeaderViewHolder(view)
            HomeRecommendationFeedViewHolder.LAYOUT -> viewHolder = HomeRecommendationFeedViewHolder(view, listener)
            BannerImageViewHolder.LAYOUT -> viewHolder = BannerImageViewHolder(view, listener)
            ReviewViewHolder.LAYOUT -> viewHolder = ReviewViewHolder(view, homeReviewListener, listener)
            PlayCardViewHolder.LAYOUT -> viewHolder = PlayCardViewHolder(view, listener)
            HomeLoadingMoreViewHolder.LAYOUT -> viewHolder = HomeLoadingMoreViewHolder(view)
            ErrorPromptViewHolder.LAYOUT -> viewHolder = ErrorPromptViewHolder(view, listener)
            PopularKeywordViewHolder.LAYOUT -> viewHolder = PopularKeywordViewHolder(view, listener, popularKeywordListener)
            CategoryWidgetViewHolder.LAYOUT -> viewHolder = CategoryWidgetViewHolder(view, listener)
            BestSellerViewHolder.LAYOUT -> viewHolder = BestSellerViewHolder(view, bestSellerListener)
            ProductHighlightComponentViewHolder.LAYOUT -> viewHolder = ProductHighlightComponentViewHolder(
                    view,
                    homeComponentListener,
                    productHighlightListener
            )
            DynamicLegoBannerViewHolder.LAYOUT -> viewHolder =
                    DynamicLegoBannerViewHolder(
                            view,
                            legoListener,
                            homeComponentListener,
                            parentRecycledViewPool
                    )
            RecommendationListCarouselViewHolder.LAYOUT -> viewHolder =
                    RecommendationListCarouselViewHolder(
                            view,
                            recommendationListCarouselListener,
                            parentRecycledViewPool
                    )
            MixLeftComponentViewHolder.LAYOUT -> viewHolder =
                    MixLeftComponentViewHolder(
                            view,
                            mixLeftComponentListener,
                            homeComponentListener,
                            parentRecycledViewPool
                    )
            MixTopComponentViewHolder.LAYOUT -> viewHolder =
                    MixTopComponentViewHolder(
                            view,
                            homeComponentListener,
                            mixTopComponentListener
                    )
            ReminderWidgetViewHolder.LAYOUT -> viewHolder =
                    ReminderWidgetViewHolder(view,reminderWidgetListener)
            TopadsBannerViewHolder.LAYOUT -> viewHolder = TopadsBannerViewHolder(view, listener)
            DynamicChannelLoadingViewHolder.LAYOUT -> viewHolder = DynamicChannelLoadingViewHolder(view)
            DynamicChannelRetryViewHolder.LAYOUT -> viewHolder = DynamicChannelRetryViewHolder(view, listener)
            Lego4AutoBannerViewHolder.LAYOUT -> viewHolder =
                    Lego4AutoBannerViewHolder(
                            view,
                            lego4AutoBannerListener,
                            homeComponentListener,
                            parentRecycledViewPool)
            FeaturedShopViewHolder.LAYOUT -> viewHolder = FeaturedShopViewHolder(
                    view,
                    featuredShopListener,
                    homeComponentListener
            )
            CategoryNavigationViewHolder.LAYOUT -> viewHolder = CategoryNavigationViewHolder(view, categoryNavigationListener)
            CarouselPlayWidgetViewHolder.LAYOUT -> viewHolder = CarouselPlayWidgetViewHolder(PlayWidgetViewHolder(view, playWidgetCoordinator), listener)
            RechargeBUWidgetMixLeftViewHolder.LAYOUT -> viewHolder =
                    RechargeBUWidgetMixLeftViewHolder(view, rechargeBUWidgetListener)
            RechargeBUWidgetMixTopViewHolder.LAYOUT -> viewHolder =
                    RechargeBUWidgetMixTopViewHolder(view, rechargeBUWidgetListener)
            BannerComponentViewHolder.LAYOUT -> viewHolder =
                    BannerComponentViewHolder(view, bannerComponentListener, homeComponentListener)
            DynamicIconViewHolder.LAYOUT -> viewHolder = DynamicIconViewHolder(view, dynamicIconComponentListener)
            ErrorStateIconViewHolder.LAYOUT -> viewHolder = ErrorStateIconViewHolder(view, listener)
            ErrorStateChannelOneViewHolder.LAYOUT -> viewHolder = ErrorStateChannelOneViewHolder(view, listener)
            ErrorStateChannelTwoViewHolder.LAYOUT -> viewHolder = ErrorStateChannelTwoViewHolder(view, listener)
            ErrorStateChannelThreeViewHolder.LAYOUT -> viewHolder = ErrorStateChannelThreeViewHolder(view, listener)
            HomeInitialShimmerViewHolder.LAYOUT -> viewHolder = HomeInitialShimmerViewHolder(view, listener)
            ShimmeringChannelViewHolder.LAYOUT -> viewHolder = ShimmeringChannelViewHolder(view, listener)
            ShimmeringIconViewHolder.LAYOUT -> viewHolder = ShimmeringIconViewHolder(view, listener)
            HomeAtfErrorViewHolder.LAYOUT -> viewHolder = HomeAtfErrorViewHolder(view, listener)
            DynamicLegoBannerSixAutoViewHolder.LAYOUT -> viewHolder =
                    DynamicLegoBannerSixAutoViewHolder(
                            view,
                            legoSixAutoListener,
                            homeComponentListener,
                            parentRecycledViewPool)
            else -> viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder
    }
}
