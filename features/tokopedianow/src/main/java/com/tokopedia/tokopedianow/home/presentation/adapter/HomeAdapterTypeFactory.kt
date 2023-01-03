package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.CategoryNavigationViewHolder
import com.tokopedia.home_component.viewholders.DynamicIconViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerSixAutoViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.FeaturedBrandViewHolder
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.Lego4AutoBannerViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.viewholders.ProductHighlightComponentViewHolder
import com.tokopedia.home_component.viewholders.QuestWidgetViewHolder
import com.tokopedia.home_component.viewholders.RecommendationListCarouselViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.CategoryNavigationDataModel
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryGridTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEmptyStateUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProgressBarUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestAllClaimedWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestTitleUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.tokopedianow.home.presentation.view.listener.DynamicLegoBannerCallback
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselAtcCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeEducationalInformationWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeEducationalInformationWidgetViewHolder.HomeEducationalInformationListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeEmptyStateViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLoadingStateViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomePlayWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder.HomeProductRecomListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProgressBarViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestAllClaimedWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestSequenceWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestTitleViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSharingWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSharingWidgetViewHolder.HomeSharingListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSwitcherViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeTickerViewHolder

class HomeAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val homeTickerListener: HomeTickerViewHolder.HomeTickerListener? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridListener? = null,
    private val bannerComponentListener: BannerComponentListener? = null,
    private val homeProductRecomOocListener: TokoNowRecommendationCarouselListener? = null,
    private val homeProductRecomListener: HomeProductRecomListener? = null,
    private val tokoNowProductCardListener: TokoNowProductCardListener? = null,
    private val homeSharingEducationListener: HomeSharingListener? = null,
    private val homeEducationalInformationListener: HomeEducationalInformationListener? = null,
    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener? = null,
    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocListener? = null,
    private val homeQuestSequenceWidgetListener : HomeQuestSequenceWidgetListener? = null,
    private val dynamicLegoBannerCallback: DynamicLegoBannerCallback? = null,
    private val homeSwitcherListener: HomeSwitcherViewHolder.HomeSwitcherListener? = null,
    private val homeLeftCarouselAtcListener: HomeLeftCarouselAtcCallback? = null,
    private val homeLeftCarouselListener: MixLeftComponentListener? = null,
    private val playWidgetCoordinator: PlayWidgetCoordinator? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null,
    private val productRecommendationBindOocListener: TokonowRecomBindPageNameListener? = null,
):  BaseAdapterTypeFactory(),
    HomeTypeFactory,
    HomeComponentTypeFactory,
    TokoNowCategoryGridTypeFactory,
    TokoNowRepurchaseTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowServerErrorTypeFactory,
    TokoNowProductRecommendationOocTypeFactory
{

    // region Common TokoNow Component
    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowRepurchaseUiModel): Int = TokoNowRepurchaseViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationOocUiModel): Int = TokoNowProductRecommendationOocViewHolder.LAYOUT
    // endregion

    // region TokoNow Home Component
    override fun type(uiModel: HomeSharingWidgetUiModel): Int = HomeSharingWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeTickerUiModel): Int = HomeTickerViewHolder.LAYOUT
    override fun type(uiModel: HomeProductRecomUiModel): Int = HomeProductRecomViewHolder.LAYOUT
    override fun type(uiModel: HomeEmptyStateUiModel): Int = HomeEmptyStateViewHolder.LAYOUT
    override fun type(uiModel: HomeLoadingStateUiModel): Int = HomeLoadingStateViewHolder.LAYOUT
    override fun type(uiModel: HomeEducationalInformationWidgetUiModel): Int = HomeEducationalInformationWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeProgressBarUiModel): Int = HomeProgressBarViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestSequenceWidgetUiModel): Int = HomeQuestSequenceWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestWidgetUiModel): Int = HomeQuestWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestTitleUiModel): Int = HomeQuestTitleViewHolder.LAYOUT
    override fun type(uiModel: HomeQuestAllClaimedWidgetUiModel): Int = HomeQuestAllClaimedWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeSwitcherUiModel): Int = HomeSwitcherViewHolder.LAYOUT
    override fun type(uiModel: HomeLeftCarouselAtcUiModel): Int = HomeLeftCarouselAtcViewHolder.LAYOUT
    override fun type(uiModel: HomePlayWidgetUiModel): Int = HomePlayWidgetViewHolder.LAYOUT
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
            TokoNowRepurchaseViewHolder.LAYOUT -> TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener, tokoNowView)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(view, tokoNowView, tokoNowChooseAddressWidgetListener)
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(view, tokoNowEmptyStateOocListener)
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
            TokoNowProductRecommendationOocViewHolder.LAYOUT -> TokoNowProductRecommendationOocViewHolder(view, homeProductRecomOocListener, productRecommendationBindOocListener)
            // endregion

            // region TokoNow Home Component
            HomeTickerViewHolder.LAYOUT -> HomeTickerViewHolder(view, homeTickerListener)
            HomeProductRecomViewHolder.LAYOUT -> HomeProductRecomViewHolder(view, homeProductRecomListener, rtrListener, rtrAnalytics)
            HomeEmptyStateViewHolder.LAYOUT -> HomeEmptyStateViewHolder(view, tokoNowView)
            HomeLoadingStateViewHolder.LAYOUT -> HomeLoadingStateViewHolder(view)
            HomeSharingWidgetViewHolder.LAYOUT -> HomeSharingWidgetViewHolder(view, homeSharingEducationListener)
            HomeEducationalInformationWidgetViewHolder.LAYOUT -> HomeEducationalInformationWidgetViewHolder(view, homeEducationalInformationListener)
            HomeProgressBarViewHolder.LAYOUT -> HomeProgressBarViewHolder(view)
            HomeQuestSequenceWidgetViewHolder.LAYOUT -> HomeQuestSequenceWidgetViewHolder(view, homeQuestSequenceWidgetListener)
            HomeQuestWidgetViewHolder.LAYOUT -> HomeQuestWidgetViewHolder(view, homeQuestSequenceWidgetListener)
            HomeQuestTitleViewHolder.LAYOUT -> HomeQuestTitleViewHolder(view, homeQuestSequenceWidgetListener)
            HomeQuestAllClaimedWidgetViewHolder.LAYOUT -> HomeQuestAllClaimedWidgetViewHolder(view, homeQuestSequenceWidgetListener)
            HomeSwitcherViewHolder.LAYOUT -> HomeSwitcherViewHolder(view, homeSwitcherListener)
            HomeLeftCarouselAtcViewHolder.LAYOUT -> HomeLeftCarouselAtcViewHolder(view, homeLeftCarouselAtcListener, rtrListener, rtrAnalytics)
            HomePlayWidgetViewHolder.LAYOUT -> HomePlayWidgetViewHolder(createPlayWidgetViewHolder(view))
            // endregion

            // region Global Home Component
            DynamicLegoBannerViewHolder.LAYOUT -> {
                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
            }
            BannerComponentViewHolder.LAYOUT -> {
                BannerComponentViewHolder(view, bannerComponentListener, null)
            }
            MixLeftComponentViewHolder.LAYOUT -> {
                MixLeftComponentViewHolder(view, homeLeftCarouselListener, null)
            }
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }

    private fun createPlayWidgetViewHolder(view: View): PlayWidgetViewHolder? {
        return playWidgetCoordinator?.let { PlayWidgetViewHolder(view, it) }
    }
}
