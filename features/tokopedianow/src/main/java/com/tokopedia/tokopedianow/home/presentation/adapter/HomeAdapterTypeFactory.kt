package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.visitable.*
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.tokopedianow.home.presentation.view.listener.DynamicLegoBannerCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.*

class HomeAdapterTypeFactory(
        private val tokoNowListener: TokoNowView? = null,
        private val homeTickerListener: HomeTickerViewHolder.HomeTickerListener? = null,
        private val homeChooseAddressWidgetListener: HomeChooseAddressWidgetViewHolder.HomeChooseAddressWidgetListener? = null,
        private val homeCategoryGridlistener: HomeCategoryGridViewHolder.HomeCategoryGridListener? = null,
        private val homeCategoryItemListener: HomeCategoryItemViewHolder.HomeCategoryItemListener? = null,
        private val bannerComponentListener: BannerComponentListener? = null,
): BaseAdapterTypeFactory(), HomeTypeFactory, HomeComponentTypeFactory {

    // region Toko Mart Home Component
    override fun type(uiModel: HomeCategoryGridUiModel): Int = HomeCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: HomeCategoryItemUiModel): Int = HomeCategoryItemViewHolder.LAYOUT
    override fun type(uiModel: HomeProductRecomUiModel): Int = HomeProductRecomViewHolder.LAYOUT

    override fun type(uiModel: HomeChooseAddressWidgetUiModel): Int = HomeChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeTickerUiModel): Int = HomeTickerViewHolder.LAYOUT
    override fun type(uiModel: HomeEmptyStateUiModel): Int = HomeEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: HomeLoadingStateUiModel): Int = HomeLoadingStateViewHolder.LAYOUT
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
            // region Toko Mart Home Component
            HomeCategoryGridViewHolder.LAYOUT -> HomeCategoryGridViewHolder(view, homeCategoryGridlistener)
            HomeCategoryItemViewHolder.LAYOUT -> HomeCategoryItemViewHolder(view, homeCategoryItemListener)
            HomeProductRecomViewHolder.LAYOUT -> HomeProductRecomViewHolder(view)
            HomeChooseAddressWidgetViewHolder.LAYOUT -> HomeChooseAddressWidgetViewHolder(view, tokoNowListener, homeChooseAddressWidgetListener)
            HomeTickerViewHolder.LAYOUT -> HomeTickerViewHolder(view, homeTickerListener)
            HomeEmptyStateViewHolder.LAYOUT -> HomeEmptyStateViewHolder(view, tokoNowListener)
            HomeLoadingStateViewHolder.LAYOUT -> HomeLoadingStateViewHolder(view)
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