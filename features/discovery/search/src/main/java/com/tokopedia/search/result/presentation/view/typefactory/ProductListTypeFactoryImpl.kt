package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder
import com.tokopedia.search.result.product.broadmatch.BroadMatchViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ChooseAddressViewHolder
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationTitleViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTitleViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTopAdsImageViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.product.suggestion.SuggestionViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TickerViewHolder
import com.tokopedia.search.result.product.broadmatch.BroadMatchListener
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.product.suggestion.SuggestionListener
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.search.result.product.ads.AdsLowOrganicTitleDataView
import com.tokopedia.search.result.product.ads.AdsLowOrganicTitleViewHolder
import com.tokopedia.search.result.product.tdn.TopAdsImageViewListener
import com.tokopedia.search.result.product.banned.BannedProductsEmptySearchDataView
import com.tokopedia.search.result.product.banned.BannedProductsEmptySearchViewHolder
import com.tokopedia.search.result.product.banner.BannerDataView
import com.tokopedia.search.result.product.banner.BannerListener
import com.tokopedia.search.result.product.banner.BannerViewHolder
import com.tokopedia.search.result.product.changeview.ChangeViewListener
import com.tokopedia.search.result.product.changeview.ViewType
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.product.cpm.BannerAdsListener
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.result.product.cpm.CpmViewHolder
import com.tokopedia.search.result.product.emptystate.EmptyStateFilterDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateFilterViewHolder
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordViewHolder
import com.tokopedia.search.result.product.emptystate.EmptyStateListener
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavListener
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavViewHolder
import com.tokopedia.search.result.product.inspirationbundle.InspirationBundleListener
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundleDataView
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundleViewHolder
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselListener
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcListener
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcViewHolder
import com.tokopedia.search.result.product.inspirationwidget.card.BigGridInspirationCardViewHolder
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.SmallGridInspirationCardViewHolder
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterDataView
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterListener
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterViewHolder
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.lastfilter.LastFilterListener
import com.tokopedia.search.result.product.lastfilter.LastFilterViewHolder
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationListener
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationViewHolder
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaListener
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder.GridInspirationProductItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder.ListInspirationProductItemViewHolder
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import com.tokopedia.search.result.product.violation.ViolationDataView
import com.tokopedia.search.result.product.violation.ViolationListener
import com.tokopedia.search.result.product.violation.ViolationViewHolder
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.video_widget.carousel.InspirationCarouselVideoViewHolder
import com.tokopedia.video_widget.carousel.InspirationVideoCarouselListener
import com.tokopedia.video_widget.carousel.VideoCarouselWidgetCoordinator
import com.tokopedia.video_widget.util.networkmonitor.NetworkMonitor

@Suppress("LongParameterList")
class ProductListTypeFactoryImpl(
    private val fragmentProvider: FragmentProvider,
    private val productListener: ProductListener,
    private val tickerListener: TickerListener,
    private val suggestionListener: SuggestionListener,
    private val globalNavListener: GlobalNavListener,
    private val bannerAdsListener: BannerAdsListener,
    private val emptyStateListener: EmptyStateListener,
    private val recommendationListener: RecommendationListener,
    private val inspirationCarouselListener: InspirationCarouselListener,
    private val broadMatchListener: BroadMatchListener,
    private val inspirationCardListener: InspirationCardListener,
    private val searchInTokopediaListener: SearchInTokopediaListener,
    private val changeViewListener: ChangeViewListener,
    private val topAdsImageViewListener: TopAdsImageViewListener,
    private val chooseAddressListener: ChooseAddressListener,
    private val bannerListener: BannerListener,
    private val lastFilterListener: LastFilterListener,
    private val inspirationFilterListener: InspirationFilterListener,
    private val violationListener: ViolationListener,
    private val videoCarouselListener: InspirationVideoCarouselListener,
    private val inspirationBundleListener: InspirationBundleListener,
    private val inspirationListAtcListener: InspirationListAtcListener,
    private val videoCarouselWidgetCoordinator: VideoCarouselWidgetCoordinator,
    private val networkMonitor: NetworkMonitor,
    private val isUsingViewStub: Boolean = false,
    private val sameSessionRecommendationListener: SameSessionRecommendationListener,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val isSneakPeekEnabled: Boolean = false,
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val inspirationProductListener: InspirationProductListener,
    private val reimagineSearch2Component: Search2Component = Search2Component.CONTROL,
) : BaseAdapterTypeFactory(), ProductListTypeFactory {

    override fun type(cpmDataView: CpmDataView): Int {
        return CpmViewHolder.LAYOUT
    }

    override fun type(tickerDataView: TickerDataView): Int {
        return TickerViewHolder.LAYOUT
    }

    override fun type(suggestionDataView: SuggestionDataView): Int {
        return SuggestionViewHolder.LAYOUT
    }

    override fun type(productItem: ProductItemDataView): Int {
        return when (changeViewListener.viewType) {
            ViewType.LIST ->
                ListProductItemViewHolder.layout(isUsingViewStub)
            ViewType.BIG_GRID ->
                BigGridProductItemViewHolder.LAYOUT
            ViewType.SMALL_GRID ->
                SmallGridProductItemViewHolder.layout(isUsingViewStub)
        }
    }

    override fun type(emptySearchProductDataView: EmptyStateKeywordDataView): Int {
        return EmptyStateKeywordViewHolder.LAYOUT
    }

    override fun type(emptySearchFilterDataView: EmptyStateFilterDataView): Int {
        return EmptyStateFilterViewHolder.LAYOUT
    }

    override fun type(globalNavDataView: GlobalNavDataView): Int {
        return GlobalNavViewHolder.LAYOUT
    }

    override fun type(inspirationCarouselDataView: InspirationCarouselDataView): Int {
        return InspirationCarouselViewHolder.LAYOUT
    }

    override fun type(inspirationCarouselDataView: InspirationCarouselVideoDataView): Int {
        return InspirationCarouselVideoViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return SearchLoadingMoreViewHolder.LAYOUT
    }

    override fun type(titleViewModel: RecommendationTitleDataView): Int {
        return RecommendationTitleViewHolder.LAYOUT
    }

    override fun type(recommendationItemDataView: RecommendationItemDataView): Int {
        return RecommendationItemViewHolder.LAYOUT
    }

    override fun type(bannedProductsEmptySearchDataView: BannedProductsEmptySearchDataView): Int {
        return BannedProductsEmptySearchViewHolder.LAYOUT
    }

    override fun type(broadMatchDataView: BroadMatchDataView): Int {
        return BroadMatchViewHolder.LAYOUT
    }

    override fun type(inspirationCardDataView: InspirationCardDataView): Int {
        return when (changeViewListener.viewType) {
            ViewType.LIST, ViewType.BIG_GRID ->
                BigGridInspirationCardViewHolder.LAYOUT
            ViewType.SMALL_GRID ->
                SmallGridInspirationCardViewHolder.LAYOUT
        }
    }

    override fun type(searchProductTitleDataView: SearchProductTitleDataView): Int {
        return SearchProductTitleViewHolder.LAYOUT
    }

    override fun type(searchInTokopediaDataView: SearchInTokopediaDataView): Int {
        return SearchInTokopediaViewHolder.LAYOUT
    }

    override fun type(searchProductTopAdsImageDataView: SearchProductTopAdsImageDataView): Int {
        return SearchProductTopAdsImageViewHolder.LAYOUT
    }

    override fun type(chooseAddressDataView: ChooseAddressDataView): Int {
        return ChooseAddressViewHolder.LAYOUT
    }

    override fun type(bannerDataView: BannerDataView): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(lastFilterDataView: LastFilterDataView): Int =
        LastFilterViewHolder.LAYOUT

    override fun type(sizeDataView: InspirationFilterDataView): Int {
        return InspirationFilterViewHolder.LAYOUT
    }

    override fun type(violationView: ViolationDataView): Int =
        ViolationViewHolder.LAYOUT

    override fun type(inspirationProductBundleDataView: InspirationProductBundleDataView): Int =
        InspirationProductBundleViewHolder.LAYOUT

    override fun type(sameSessionRecommendationDataView: SameSessionRecommendationDataView): Int =
        SameSessionRecommendationViewHolder.LAYOUT

    override fun type(inspirationListAtcDataView: InspirationListAtcDataView): Int =
        InspirationListAtcViewHolder.LAYOUT

    override fun type(adsLowOrganicTitleDataView: AdsLowOrganicTitleDataView): Int =
        AdsLowOrganicTitleViewHolder.LAYOUT

    override fun type(inspirationKeywordCardView: InspirationKeywordCardView): Int =
        InspirationKeywordViewHolder.LAYOUT

    override fun type(inspirationProductCardView: InspirationProductItemDataView): Int {
        return when (changeViewListener.viewType) {
            ViewType.LIST ->
                ListInspirationProductItemViewHolder.layout(isUsingViewStub)
            else ->
                GridInspirationProductItemViewHolder.layout(isUsingViewStub)
        }
    }

    @Suppress("ComplexMethod")
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ListProductItemViewHolder.LAYOUT, ListProductItemViewHolder.LAYOUT_WITH_VIEW_STUB ->
                ListProductItemViewHolder(view, productListener, isSneakPeekEnabled)
            SmallGridProductItemViewHolder.LAYOUT, SmallGridProductItemViewHolder.LAYOUT_WITH_VIEW_STUB ->
                SmallGridProductItemViewHolder(view, productListener, isSneakPeekEnabled)
            BigGridProductItemViewHolder.LAYOUT ->
                BigGridProductItemViewHolder(view, productListener, isSneakPeekEnabled)
            CpmViewHolder.LAYOUT -> CpmViewHolder(view, bannerAdsListener)
            TickerViewHolder.LAYOUT -> TickerViewHolder(view, tickerListener)
            SuggestionViewHolder.LAYOUT -> SuggestionViewHolder(view, suggestionListener)
            EmptyStateKeywordViewHolder.LAYOUT -> EmptyStateKeywordViewHolder(view, emptyStateListener)
            EmptyStateFilterViewHolder.LAYOUT -> EmptyStateFilterViewHolder(view, emptyStateListener)
            GlobalNavViewHolder.LAYOUT -> GlobalNavViewHolder(view, globalNavListener)
            InspirationCarouselViewHolder.LAYOUT ->
                InspirationCarouselViewHolder(
                    view,
                    inspirationCarouselListener,
                    recycledViewPool,
                    reimagineSearch2Component.isReimagineCarousel(),
                )
            InspirationCarouselVideoViewHolder.LAYOUT -> InspirationCarouselVideoViewHolder(
                    view,
                    videoCarouselListener,
                    videoCarouselWidgetCoordinator,
                    networkMonitor
                )
            InspirationProductBundleViewHolder.LAYOUT -> InspirationProductBundleViewHolder(
                view,
                inspirationBundleListener,
            )
            SearchLoadingMoreViewHolder.LAYOUT -> SearchLoadingMoreViewHolder(view)
            RecommendationTitleViewHolder.LAYOUT -> RecommendationTitleViewHolder(view)
            RecommendationItemViewHolder.LAYOUT ->
                RecommendationItemViewHolder(view, recommendationListener)
            BannedProductsEmptySearchViewHolder.LAYOUT -> BannedProductsEmptySearchViewHolder(view)
            BroadMatchViewHolder.LAYOUT ->
                BroadMatchViewHolder(
                    view,
                    broadMatchListener,
                    recycledViewPool,
                    reimagineSearch2Component.isReimagineCarousel(),
                )
            SmallGridInspirationCardViewHolder.LAYOUT ->
                SmallGridInspirationCardViewHolder(view, inspirationCardListener)
            BigGridInspirationCardViewHolder.LAYOUT ->
                BigGridInspirationCardViewHolder(view, inspirationCardListener)
            SearchProductTitleViewHolder.LAYOUT -> SearchProductTitleViewHolder(view)
            SearchInTokopediaViewHolder.LAYOUT ->
                SearchInTokopediaViewHolder(view, searchInTokopediaListener)
            SearchProductTopAdsImageViewHolder.LAYOUT ->
                SearchProductTopAdsImageViewHolder(view, topAdsImageViewListener)
            ChooseAddressViewHolder.LAYOUT ->
                ChooseAddressViewHolder(
                    view,
                    chooseAddressListener,
                    changeViewListener,
                    fragmentProvider,
                )
            BannerViewHolder.LAYOUT -> BannerViewHolder(view, bannerListener)
            LastFilterViewHolder.LAYOUT -> LastFilterViewHolder(view, lastFilterListener)
            InspirationFilterViewHolder.LAYOUT -> InspirationFilterViewHolder(view, inspirationFilterListener)
            ViolationViewHolder.LAYOUT -> ViolationViewHolder(view, violationListener)
            SameSessionRecommendationViewHolder.LAYOUT -> SameSessionRecommendationViewHolder(
                view,
                inspirationCarouselListener,
                sameSessionRecommendationListener,
            )
            InspirationListAtcViewHolder.LAYOUT ->
                InspirationListAtcViewHolder(view, inspirationListAtcListener, recycledViewPool)
            AdsLowOrganicTitleViewHolder.LAYOUT ->
                AdsLowOrganicTitleViewHolder(view)
            InspirationKeywordViewHolder.LAYOUT ->
                InspirationKeywordViewHolder(view, inspirationKeywordListener, changeViewListener)
            GridInspirationProductItemViewHolder.LAYOUT, GridInspirationProductItemViewHolder.LAYOUT_WITH_VIEW_STUB ->
                GridInspirationProductItemViewHolder(view, inspirationProductListener, productListener)
            ListInspirationProductItemViewHolder.LAYOUT, ListInspirationProductItemViewHolder.LAYOUT_WITH_VIEW_STUB ->
                ListInspirationProductItemViewHolder(view, inspirationProductListener, productListener)

            else -> super.createViewHolder(view, type)
        }
    }
}
