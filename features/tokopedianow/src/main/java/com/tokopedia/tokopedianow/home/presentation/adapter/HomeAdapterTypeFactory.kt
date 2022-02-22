package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
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
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.tokopedianow.home.presentation.view.listener.DynamicLegoBannerCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.*
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeEducationalInformationWidgetViewHolder.HomeEducationalInformationListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder.HomeProductRecomListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSharingEducationWidgetViewHolder.HomeSharingEducationListener

class HomeAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val homeTickerListener: HomeTickerViewHolder.HomeTickerListener? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridListener? = null,
    private val bannerComponentListener: BannerComponentListener? = null,
    private val homeProductRecomListener: HomeProductRecomListener? = null,
    private val tokoNowProductCardListener: TokoNowProductCardListener? = null,
    private val homeSharingEducationListener: HomeSharingEducationListener? = null,
    private val homeEducationalInformationListener: HomeEducationalInformationListener? = null,
    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener? = null,
    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocListener? = null,
    private val homeQuestSequenceWidgetListener : HomeQuestSequenceWidgetListener? = null,
    private val mixLeftComponentListener: MixLeftComponentListener? = null,
    private val dynamicLegoBannerCallback: DynamicLegoBannerCallback? = null,
    private val homeSwitcherListener: HomeSwitcherViewHolder.HomeSwitcherListener? = null
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
    override fun type(uiModel: HomeQuestSequenceWidgetUiModel): Int = HomeQuestSequenceWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestWidgetUiModel): Int = HomeQuestWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestTitleUiModel): Int = HomeQuestTitleViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestAllClaimedWidgetUiModel): Int = HomeQuestAllClaimedWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeSwitcherUiModel): Int = HomeSwitcherViewHolder.LAYOUT
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
    override fun type(questWidgetModel: QuestWidgetModel): Int = QuestWidgetViewHolder.LAYOUT
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
            HomeQuestSequenceWidgetViewHolder.LAYOUT -> HomeQuestSequenceWidgetViewHolder(view, homeQuestSequenceWidgetListener)
            HomeQuestWidgetViewHolder.LAYOUT -> HomeQuestWidgetViewHolder(view, homeQuestSequenceWidgetListener)
            HomeQuestTitleViewHolder.LAYOUT -> HomeQuestTitleViewHolder(view, homeQuestSequenceWidgetListener)
            HomeQuestAllClaimedWidgetViewHolder.LAYOUT -> HomeQuestAllClaimedWidgetViewHolder(view, homeQuestSequenceWidgetListener)
            HomeSwitcherViewHolder.LAYOUT -> HomeSwitcherViewHolder(view, homeSwitcherListener)
            // endregion

            // region Global Home Component
            DynamicLegoBannerViewHolder.LAYOUT -> {
                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
            }
            BannerComponentViewHolder.LAYOUT -> {
                BannerComponentViewHolder(view, bannerComponentListener, null)
            }
            MixLeftComponentViewHolder.LAYOUT -> {
                MixLeftComponentViewHolder(view, mixLeftComponentListener, null)
            }
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }
}