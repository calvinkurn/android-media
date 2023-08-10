package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowBundleTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationOocTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowServerErrorTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowBundleWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowBundleWidgetViewHolder.TokoNowBundleWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductCarouselChipsViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseProductViewHolder.TokoNowRepurchaseProductListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseViewHolder
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
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemShimmeringUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.view.HomeProductCarouselChipsView.HomeProductCarouselChipsViewListener
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
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemShimmeringViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemTracker
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon.HomeClaimCouponWidgetViewHolder.HomeClaimCouponWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeHeaderViewHolder

class HomeAdapterTypeFactory(
    private val tokoNowView: TokoNowView? = null,
    private val tokoNowChooseAddressWidgetListener: TokoNowChooseAddressWidgetListener? = null,
    private val tokoNowCategoryMenuListener: TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener? = null,
    private val bannerComponentListener: BannerComponentListener? = null,
    private val homeProductRecomOocListener: TokoNowRecommendationCarouselListener? = null,
    private val homeProductRecomListener: HomeProductRecomListener? = null,
    private val tokoNowProductCardListener: TokoNowProductCardListener? = null,
    private val tokoNowRepurchaseListener: TokoNowRepurchaseProductListener? = null,
    private val homeSharingEducationListener: HomeSharingListener? = null,
    private val homeEducationalInformationListener: HomeEducationalInformationListener? = null,
    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener? = null,
    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocListener? = null,
    private val homeQuestSequenceWidgetListener: HomeQuestSequenceWidgetListener? = null,
    private val dynamicLegoBannerCallback: DynamicLegoBannerCallback? = null,
    private val homeSwitcherListener: HomeSwitcherViewHolder.HomeSwitcherListener? = null,
    private val homeLeftCarouselAtcListener: HomeLeftCarouselAtcCallback? = null,
    private val homeLeftCarouselListener: MixLeftComponentListener? = null,
    private val playWidgetCoordinator: PlayWidgetCoordinator? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null,
    private val productRecommendationBindOocListener: TokonowRecomBindPageNameListener? = null,
    private val claimCouponWidgetItemListener: HomeClaimCouponWidgetItemListener? = null,
    private val claimCouponWidgetItemTracker: HomeClaimCouponWidgetItemTracker? = null,
    private val claimCouponWidgetListener: HomeClaimCouponWidgetListener? = null,
    private val productCarouselChipListener: HomeProductCarouselChipsViewListener? = null,
    private val productBundleWidgetListener: ProductBundleWidgetListener? = null,
    private val tokoNowBundleWidgetListener: TokoNowBundleWidgetListener? = null
):  BaseAdapterTypeFactory(),
    HomeTypeFactory,
    HomeComponentTypeFactory,
    TokoNowTickerTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowRepurchaseTypeFactory,
    com.tokopedia.tokopedianow.common.adapter.oldrepurchase.TokoNowRepurchaseTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowEmptyStateOocTypeFactory,
    TokoNowServerErrorTypeFactory,
    TokoNowProductRecommendationOocTypeFactory,
    TokoNowBundleTypeFactory
{

    // region Common TokoNow Component
    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT
    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT
    override fun type(uiModel: TokoNowRepurchaseUiModel): Int = TokoNowRepurchaseViewHolder.LAYOUT
    override fun type(uiModel: com.tokopedia.tokopedianow.common.model.oldrepurchase.TokoNowRepurchaseUiModel): Int = com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowRepurchaseViewHolder.LAYOUT
    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT
    override fun type(uiModel: TokoNowProductRecommendationOocUiModel): Int = TokoNowProductRecommendationOocViewHolder.LAYOUT
    override fun type(uiModel: TokoNowTickerUiModel): Int = TokoNowTickerViewHolder.LAYOUT
    override fun type(uiModel: TokoNowBundleUiModel): Int = TokoNowBundleWidgetViewHolder.LAYOUT
    // endregion

    // region TokoNow Home Component
    override fun type(uiModel: HomeSharingWidgetUiModel): Int = HomeSharingWidgetViewHolder.LAYOUT
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
    override fun type(uiModel: HomeClaimCouponWidgetItemUiModel): Int = HomeClaimCouponWidgetItemViewHolder.LAYOUT
    override fun type(uiModel: HomeClaimCouponWidgetUiModel): Int = HomeClaimCouponWidgetViewHolder.LAYOUT
    override fun type(uiModel: HomeClaimCouponWidgetItemShimmeringUiModel): Int = HomeClaimCouponWidgetItemShimmeringViewHolder.LAYOUT
    override fun type(uiModel: HomeProductCarouselChipsUiModel): Int = HomeProductCarouselChipsViewHolder.LAYOUT
    override fun type(uiModel: HomeHeaderUiModel): Int = HomeHeaderViewHolder.LAYOUT
    // endregion

    // region Global Home Component
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT
    override fun type(mixLeftDataModel: MixLeftDataModel): Int = MixLeftComponentViewHolder.LAYOUT
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            // region Common TokoNow Component
            TokoNowCategoryMenuViewHolder.LAYOUT -> TokoNowCategoryMenuViewHolder(view, tokoNowCategoryMenuListener)
            TokoNowRepurchaseViewHolder.LAYOUT -> TokoNowRepurchaseViewHolder(view, tokoNowRepurchaseListener, tokoNowView)
            com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowRepurchaseViewHolder.LAYOUT -> com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener, tokoNowView)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(view, tokoNowView, tokoNowChooseAddressWidgetListener)
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(view, tokoNowEmptyStateOocListener)
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
            TokoNowProductRecommendationOocViewHolder.LAYOUT -> TokoNowProductRecommendationOocViewHolder(view, homeProductRecomOocListener, productRecommendationBindOocListener)
            TokoNowTickerViewHolder.LAYOUT -> TokoNowTickerViewHolder(view)
            TokoNowBundleWidgetViewHolder.LAYOUT -> TokoNowBundleWidgetViewHolder(view, productBundleWidgetListener, tokoNowBundleWidgetListener)
            // endregion

            // region TokoNow Home Component
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
            HomeClaimCouponWidgetItemViewHolder.LAYOUT -> HomeClaimCouponWidgetItemViewHolder(view, claimCouponWidgetItemListener, claimCouponWidgetItemTracker)
            HomeClaimCouponWidgetViewHolder.LAYOUT -> HomeClaimCouponWidgetViewHolder(view, claimCouponWidgetItemListener, claimCouponWidgetItemTracker, claimCouponWidgetListener)
            HomeClaimCouponWidgetItemShimmeringViewHolder.LAYOUT -> HomeClaimCouponWidgetItemShimmeringViewHolder(view)
            HomeProductCarouselChipsViewHolder.LAYOUT -> HomeProductCarouselChipsViewHolder(view, productCarouselChipListener)
            HomeHeaderViewHolder.LAYOUT -> HomeHeaderViewHolder(view)
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
