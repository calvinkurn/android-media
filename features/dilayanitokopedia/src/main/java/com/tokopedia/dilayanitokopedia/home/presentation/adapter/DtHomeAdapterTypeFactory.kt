package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dilayanitokopedia.common.adapter.typefactory.DtChooseAddressWidgetTypeFactory
import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel
import com.tokopedia.dilayanitokopedia.common.view.DtView
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.DtChooseAddressWidgetViewHolder
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
import timber.log.Timber


class DtHomeAdapterTypeFactory(
    private val dtView: DtView? = null,
//    private val homeTickerListener: HomeTickerViewHolder.HomeTickerListener? = null,
    private val dtChooseAddressWidgetListener: DtChooseAddressWidgetViewHolder.DtChooseAddressWidgetListener? = null,
//    private val tokoNowCategoryGridListener: TokoNowCategoryGridListener? = null,
    private val bannerComponentListener: BannerComponentListener? = null,
//    private val homeProductRecomListener: HomeProductRecomListener? = null,
//    private val tokoNowProductCardListener: TokoNowProductCardListener? = null,
//    private val homeSharingEducationListener: HomeSharingListener? = null,
//    private val homeEducationalInformationListener: HomeEducationalInformationListener? = null,
//    private val serverErrorListener: TokoNowServerErrorViewHolder.ServerErrorListener? = null,
//    private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocListener? = null,
//    private val homeQuestSequenceWidgetListener : HomeQuestSequenceWidgetListener? = null,
//    private val dynamicLegoBannerCallback: DynamicLegoBannerCallback? = null,
//    private val homeSwitcherListener: HomeSwitcherViewHolder.HomeSwitcherListener? = null,
//    private val homeLeftCarouselAtcListener: HomeLeftCarouselAtcCallback? = null,
    private val homeLeftCarouselListener: MixLeftComponentListener? = null,
//    private val playWidgetCoordinator: PlayWidgetCoordinator? = null
) : BaseAdapterTypeFactory(),
//    HomeTypeFactory,
    HomeComponentTypeFactory,
//    TokoNowCategoryGridTypeFactory,
//    TokoNowRepurchaseTypeFactory,
    DtChooseAddressWidgetTypeFactory
//    TokoNowEmptyStateOocTypeFactory,
//    TokoNowServerErrorTypeFactory
{
    init {
        Timber.d("logging--DtHomeAdapterTypeFactory called")

    }

    //    // region Common TokoNow Component
//    override fun type(uiModel: TokoNowCategoryGridUiModel): Int = TokoNowCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: DtChooseAddressWidgetUiModel): Int = DtChooseAddressWidgetViewHolder.LAYOUT
//    override fun type(uiModel: TokoNowRepurchaseUiModel): Int = TokoNowRepurchaseViewHolder.LAYOUT
//    override fun type(uiModel: TokoNowEmptyStateOocUiModel): Int = TokoNowEmptyStateOocViewHolder.LAYOUT
//    override fun type(uiModel: TokoNowServerErrorUiModel): Int = TokoNowServerErrorViewHolder.LAYOUT
//    // endregion

//    // region TokoNow Home Component
//    override fun type(uiModel: HomeSharingWidgetUiModel): Int = HomeSharingWidgetViewHolder.LAYOUT
//    override fun type(uiModel: HomeTickerUiModel): Int = HomeTickerViewHolder.LAYOUT
//    override fun type(uiModel: HomeProductRecomUiModel): Int = HomeProductRecomViewHolder.LAYOUT
//    override fun type(uiModel: HomeEmptyStateUiModel): Int = HomeEmptyStateViewHolder.LAYOUT
//    override fun type(uiModel: HomeLoadingStateUiModel): Int = HomeLoadingStateViewHolder.LAYOUT
//    override fun type(uiModel: HomeEducationalInformationWidgetUiModel): Int = HomeEducationalInformationWidgetViewHolder.LAYOUT
//    override fun type(uiModel: HomeProgressBarUiModel): Int = HomeProgressBarViewHolder.LAYOUT
//    override fun type(uiModel: HomeQuestSequenceWidgetUiModel): Int = HomeQuestSequenceWidgetViewHolder.LAYOUT
//    override fun type(uiModel: HomeQuestWidgetUiModel): Int = HomeQuestWidgetViewHolder.LAYOUT
//    override fun type(uiModel: HomeQuestTitleUiModel): Int = HomeQuestTitleViewHolder.LAYOUT
//    override fun type(uiModel: HomeQuestAllClaimedWidgetUiModel): Int = HomeQuestAllClaimedWidgetViewHolder.LAYOUT
//    override fun type(uiModel: HomeSwitcherUiModel): Int = HomeSwitcherViewHolder.LAYOUT
//    override fun type(uiModel: HomeLeftCarouselAtcUiModel): Int = HomeLeftCarouselAtcViewHolder.LAYOUT
//    override fun type(uiModel: HomePlayWidgetUiModel): Int = HomePlayWidgetViewHolder.LAYOUT
//    // endregion

    // region Global Home Component
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT
    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int =
        DynamicLegoBannerSixAutoViewHolder.LAYOUT

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int =
        RecommendationListCarouselViewHolder.LAYOUT

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int = ReminderWidgetViewHolder.LAYOUT
    override fun type(mixTopDataModel: MixTopDataModel): Int = MixTopComponentViewHolder.LAYOUT
    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int = ProductHighlightComponentViewHolder.LAYOUT
    override fun type(lego4AutoDataModel: Lego4AutoDataModel) = Lego4AutoBannerViewHolder.LAYOUT
    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int = FeaturedShopViewHolder.LAYOUT
    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int = CategoryNavigationViewHolder.LAYOUT
    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int = DynamicIconViewHolder.LAYOUT
    override fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int = FeaturedBrandViewHolder.LAYOUT
    override fun type(questWidgetModel: QuestWidgetModel): Int = QuestWidgetViewHolder.LAYOUT

    //current used
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    override fun type(mixLeftDataModel: MixLeftDataModel): Int = MixLeftComponentViewHolder.LAYOUT


    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        Timber.d("logging--DtHomeAdapterTypeFactory-createViewHolder called")

        return when (type) {
//            // region Common TokoNow Component
//            TokoNowCategoryGridViewHolder.LAYOUT -> TokoNowCategoryGridViewHolder(view, tokoNowCategoryGridListener)
//            TokoNowRepurchaseViewHolder.LAYOUT -> TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener, tokoNowView)
            DtChooseAddressWidgetViewHolder.LAYOUT -> DtChooseAddressWidgetViewHolder(view, dtView, dtChooseAddressWidgetListener)
//            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(view, tokoNowEmptyStateOocListener)
//            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
//            // endregion

//            // region TokoNow Home Component
//            HomeTickerViewHolder.LAYOUT -> HomeTickerViewHolder(view, homeTickerListener)
//            HomeProductRecomViewHolder.LAYOUT -> HomeProductRecomViewHolder(view, tokoNowView, homeProductRecomListener)
//            HomeEmptyStateViewHolder.LAYOUT -> HomeEmptyStateViewHolder(view, tokoNowView)
//            HomeLoadingStateViewHolder.LAYOUT -> HomeLoadingStateViewHolder(view)
//            HomeSharingWidgetViewHolder.LAYOUT -> HomeSharingWidgetViewHolder(view, homeSharingEducationListener)
//            HomeEducationalInformationWidgetViewHolder.LAYOUT -> HomeEducationalInformationWidgetViewHolder(view, homeEducationalInformationListener)
//            HomeProgressBarViewHolder.LAYOUT -> HomeProgressBarViewHolder(view)
//            HomeQuestSequenceWidgetViewHolder.LAYOUT -> HomeQuestSequenceWidgetViewHolder(view, homeQuestSequenceWidgetListener)
//            HomeQuestWidgetViewHolder.LAYOUT -> HomeQuestWidgetViewHolder(view, homeQuestSequenceWidgetListener)
//            HomeQuestTitleViewHolder.LAYOUT -> HomeQuestTitleViewHolder(view, homeQuestSequenceWidgetListener)
//            HomeQuestAllClaimedWidgetViewHolder.LAYOUT -> HomeQuestAllClaimedWidgetViewHolder(view, homeQuestSequenceWidgetListener)
//            HomeSwitcherViewHolder.LAYOUT -> HomeSwitcherViewHolder(view, homeSwitcherListener)
//            HomeLeftCarouselAtcViewHolder.LAYOUT -> HomeLeftCarouselAtcViewHolder(view, homeLeftCarouselAtcListener, tokoNowView)
//            HomePlayWidgetViewHolder.LAYOUT -> HomePlayWidgetViewHolder(createPlayWidgetViewHolder(view))
//            // endregion

            // region Global Home Component
//            DynamicLegoBannerViewHolder.LAYOUT -> {
//                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
//            }
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

//    private fun createPlayWidgetViewHolder(view: View): PlayWidgetViewHolder? {
//        return playWidgetCoordinator?.let { PlayWidgetViewHolder(view, it) }
//    }
}