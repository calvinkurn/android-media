package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.visitable.*
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.tokopedianow.home.presentation.view.listener.DynamicLegoBannerCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.*

class HomeAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val homeTickerListener: HomeTickerViewHolder.HomeTickerListener? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener? = null,
    private val bannerComponentListener: BannerComponentListener? = null,
    private val homeProductRecomListener: HomeProductRecomViewHolder.HomeProductRecomListener? = null,
    private val tokoNowProductCardListener: TokoNowProductCardViewHolder.TokoNowProductCardListener? = null,
    private val homeSharingEducationListener: HomeSharingEducationWidgetViewHolder.HomeSharingEducationListener? = null,
    private val homeEducationalInformationListener: HomeEducationalInformationWidgetViewHolder.HomeEducationalInformationListener? = null,
    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener,
    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener? = null
):  BaseAdapterTypeFactory(),
    HomeTypeFactory,
    HomeComponentTypeFactory,
    TokoNowCategoryGridTypeFactory,
    TokoNowRepurchaseTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowServerErrorTypeFactory {

    // region Common TokoNow Component
    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowRepurchaseUiModel): Int = TokoNowRepurchaseViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT
    // endregion

    // region TokoNow Home Component
    override fun type(uiModel: HomeTickerUiModel): Int = HomeTickerViewHolder.LAYOUT
    override fun type(uiModel: HomeProductRecomUiModel): Int = HomeProductRecomViewHolder.LAYOUT
    override fun type(uiModel: HomeEmptyStateUiModel): Int = HomeEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: HomeLoadingStateUiModel): Int = HomeLoadingStateViewHolder.LAYOUT
    override fun type(uiModel: HomeSharingEducationWidgetUiModel): Int = HomeSharingEducationWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeEducationalInformationWidgetUiModel): Int = HomeEducationalInformationWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeProgressBarUiModel): Int = HomeProgressBarViewHolder.LAYOUT
    // endregion

    // region Global Home Component
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT
    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int = DynamicLegoBannerSixAutoViewHolder.LAYOUT
    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int = RecommendationListCarouselViewHolder.LAYOUT
    override fun type(reminderWidgetModel: ReminderWidgetModel): Int = ReminderWidgetViewHolder.LAYOUT
    override fun type(mixLeftDataModel: MixLeftDataModel): Int = MixLeftComponentViewHolder.LAYOUT
    override fun type(mixTopDataModel: MixTopDataModel): Int = MixTopComponentViewHolder.LAYOUT
    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int = ProductHighlightComponentViewHolder.LAYOUT
    override fun type(lego4AutoDataModel: Lego4AutoDataModel) = Lego4AutoBannerViewHolder.LAYOUT
    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int = FeaturedShopViewHolder.LAYOUT
    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int = CategoryNavigationViewHolder.LAYOUT
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int = DynamicIconViewHolder.LAYOUT
    override fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int = FeaturedBrandViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // region Common TokoNow Component
            TokoNowCategoryGridViewHolder.LAYOUT -> TokoNowCategoryGridViewHolder(view, tokoNowCategoryGridListener)
            TokoNowRepurchaseViewHolder.LAYOUT -> TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(view, tokoNowView, tokoNowChooseAddressWidgetListener)
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(view, tokoNowEmptyStateOocListener)
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
            // endregion

            // region TokoNow Home Component
            HomeTickerViewHolder.LAYOUT -> HomeTickerViewHolder(view, homeTickerListener)
            HomeProductRecomViewHolder.LAYOUT -> HomeProductRecomViewHolder(view, tokoNowView, homeProductRecomListener)
            HomeEmptyStateViewHolder.LAYOUT -> HomeEmptyStateViewHolder(view, tokoNowView)
            HomeLoadingStateViewHolder.LAYOUT -> HomeLoadingStateViewHolder(view)
            HomeSharingEducationWidgetViewHolder.LAYOUT -> HomeSharingEducationWidgetViewHolder(view, homeSharingEducationListener)
            HomeEducationalInformationWidgetViewHolder.LAYOUT -> HomeEducationalInformationWidgetViewHolder(view, homeEducationalInformationListener)
            HomeProgressBarViewHolder.LAYOUT -> HomeProgressBarViewHolder(view)
            // endregion

            // region Global Home Component
            DynamicLegoBannerViewHolder.LAYOUT -> {
                val listener = DynamicLegoBannerCallback(view.context)
                DynamicLegoBannerViewHolder(view, listener, null)
            }
            BannerComponentViewHolder.LAYOUT -> BannerComponentViewHolder(view, bannerComponentListener, null)
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }
}