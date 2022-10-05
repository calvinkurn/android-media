package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BroadMatchViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ChooseAddressViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationTitleViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTitleViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTopAdsImageViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SuggestionViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TickerViewHolder
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener
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
import com.tokopedia.search.result.product.inspirationwidget.card.BigGridInspirationCardViewHolder
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.SmallGridInspirationCardViewHolder
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeListener
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeViewHolder
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.lastfilter.LastFilterListener
import com.tokopedia.search.result.product.lastfilter.LastFilterViewHolder
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationListener
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationViewHolder
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaListener
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaViewHolder
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
    private val inspirationSizeListener: InspirationSizeListener,
    private val violationListener: ViolationListener,
    private val videoCarouselListener: InspirationVideoCarouselListener,
    private val inspirationBundleListener: InspirationBundleListener,
    private val videoCarouselWidgetCoordinator: VideoCarouselWidgetCoordinator,
    private val networkMonitor: NetworkMonitor,
    private val isUsingViewStub: Boolean = false,
    private val sameSessionRecommendationListener: SameSessionRecommendationListener,
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

    override fun type(sizeDataView: InspirationSizeDataView): Int {
        return InspirationSizeViewHolder.LAYOUT
    }

    override fun type(violationView: ViolationDataView): Int =
        ViolationViewHolder.LAYOUT

    override fun type(inspirationProductBundleDataView: InspirationProductBundleDataView): Int =
        InspirationProductBundleViewHolder.LAYOUT

    override fun type(sameSessionRecommendationDataView: SameSessionRecommendationDataView): Int =
        SameSessionRecommendationViewHolder.LAYOUT

    @Suppress("ComplexMethod")
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ListProductItemViewHolder.LAYOUT, ListProductItemViewHolder.LAYOUT_WITH_VIEW_STUB ->
                ListProductItemViewHolder(view, productListener)
            SmallGridProductItemViewHolder.LAYOUT, SmallGridProductItemViewHolder.LAYOUT_WITH_VIEW_STUB ->
                SmallGridProductItemViewHolder(view, productListener)
            BigGridProductItemViewHolder.LAYOUT ->
                BigGridProductItemViewHolder(view, productListener)
            CpmViewHolder.LAYOUT -> CpmViewHolder(view, bannerAdsListener)
            TickerViewHolder.LAYOUT -> TickerViewHolder(view, tickerListener)
            SuggestionViewHolder.LAYOUT -> SuggestionViewHolder(view, suggestionListener)
            EmptyStateKeywordViewHolder.LAYOUT -> EmptyStateKeywordViewHolder(view, emptyStateListener)
            EmptyStateFilterViewHolder.LAYOUT -> EmptyStateFilterViewHolder(view, emptyStateListener)
            GlobalNavViewHolder.LAYOUT -> GlobalNavViewHolder(view, globalNavListener)
            InspirationCarouselViewHolder.LAYOUT ->
                InspirationCarouselViewHolder(view, inspirationCarouselListener)
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
            BroadMatchViewHolder.LAYOUT -> BroadMatchViewHolder(view, broadMatchListener)
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
            InspirationSizeViewHolder.LAYOUT -> InspirationSizeViewHolder(view, inspirationSizeListener)
            ViolationViewHolder.LAYOUT -> ViolationViewHolder(view, violationListener)
            SameSessionRecommendationViewHolder.LAYOUT -> SameSessionRecommendationViewHolder(
                view,
                inspirationCarouselListener,
                sameSessionRecommendationListener,
            )

            else -> super.createViewHolder(view, type)
        }
    }
}
