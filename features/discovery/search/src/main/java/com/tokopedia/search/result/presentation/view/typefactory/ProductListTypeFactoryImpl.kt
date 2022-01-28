package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.result.presentation.model.BannedProductsEmptySearchDataView
import com.tokopedia.search.result.presentation.model.BannedProductsTickerDataView
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LastFilterDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.presentation.model.SeparatorDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsEmptySearchViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannedProductsTickerViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BannerViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BigGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.BroadMatchViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ChooseAddressViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.CpmViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.InspirationCarouselViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.LastFilterViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ListProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationTitleViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductCountViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTitleViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SearchProductTopAdsImageViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SeparatorViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SuggestionViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.TickerViewHolder
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.BannerListener
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import com.tokopedia.search.result.presentation.view.listener.ChooseAddressListener
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.search.result.presentation.view.listener.LastFilterListener
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener
import com.tokopedia.search.result.presentation.view.listener.TickerListener
import com.tokopedia.search.result.presentation.view.listener.TopAdsImageViewListener
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateListener
import com.tokopedia.search.result.product.emptystate.EmptyStateViewHolder
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavListener
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavViewHolder
import com.tokopedia.search.result.product.inspirationwidget.card.BigGridInspirationCardViewHolder
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardListener
import com.tokopedia.search.result.product.inspirationwidget.card.SmallGridInspirationCardViewHolder
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeListener
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeViewHolder
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaListener
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaViewHolder

@Suppress("LongParameterList")
class ProductListTypeFactoryImpl(
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
    private val searchNavigationListener: SearchNavigationClickListener,
    private val topAdsImageViewListener: TopAdsImageViewListener,
    private val chooseAddressListener: ChooseAddressListener,
    private val bannerListener: BannerListener,
    private val lastFilterListener: LastFilterListener,
    private val inspirationSizeListener: InspirationSizeListener,
) : BaseAdapterTypeFactory(), ProductListTypeFactory {

    override var recyclerViewItem = 0

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
        return when (recyclerViewItem) {
            SearchConstant.RecyclerView.VIEW_LIST ->
                ListProductItemViewHolder.LAYOUT
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID ->
                BigGridProductItemViewHolder.LAYOUT
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID ->
                SmallGridProductItemViewHolder.LAYOUT
            else -> SmallGridProductItemViewHolder.LAYOUT
        }
    }

    override fun type(emptySearchProductDataView: EmptyStateDataView): Int {
        return EmptyStateViewHolder.LAYOUT
    }

    override fun type(globalNavDataView: GlobalNavDataView): Int {
        return GlobalNavViewHolder.LAYOUT
    }

    override fun type(inspirationCarouselDataView: InspirationCarouselDataView): Int {
        return InspirationCarouselViewHolder.LAYOUT
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

    override fun type(bannedProductsTickerDataView: BannedProductsTickerDataView): Int {
        return BannedProductsTickerViewHolder.LAYOUT
    }

    override fun type(broadMatchDataView: BroadMatchDataView): Int {
        return BroadMatchViewHolder.LAYOUT
    }

    override fun type(inspirationCardDataView: InspirationCardDataView): Int {
        return when (recyclerViewItem) {
            SearchConstant.RecyclerView.VIEW_LIST,
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID ->
                BigGridInspirationCardViewHolder.LAYOUT
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID ->
                SmallGridInspirationCardViewHolder.LAYOUT
            else -> SmallGridInspirationCardViewHolder.LAYOUT
        }
    }

    override fun type(separatorDataView: SeparatorDataView): Int {
        return SeparatorViewHolder.LAYOUT
    }

    override fun type(searchProductTitleDataView: SearchProductTitleDataView): Int {
        return SearchProductTitleViewHolder.LAYOUT
    }

    override fun type(searchInTokopediaDataView: SearchInTokopediaDataView): Int {
        return SearchInTokopediaViewHolder.LAYOUT
    }

    override fun type(searchProductCountDataView: SearchProductCountDataView): Int {
        return SearchProductCountViewHolder.LAYOUT
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
    @Suppress("ComplexMethod")
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ListProductItemViewHolder.LAYOUT ->
                ListProductItemViewHolder(view, productListener)
            SmallGridProductItemViewHolder.LAYOUT ->
                SmallGridProductItemViewHolder(view, productListener)
            BigGridProductItemViewHolder.LAYOUT ->
                BigGridProductItemViewHolder(view, productListener)
            CpmViewHolder.LAYOUT -> CpmViewHolder(view, bannerAdsListener)
            TickerViewHolder.LAYOUT -> TickerViewHolder(view, tickerListener)
            SuggestionViewHolder.LAYOUT -> SuggestionViewHolder(view, suggestionListener)
            EmptyStateViewHolder.LAYOUT -> EmptyStateViewHolder(view, emptyStateListener)
            GlobalNavViewHolder.LAYOUT -> GlobalNavViewHolder(view, globalNavListener)
            InspirationCarouselViewHolder.LAYOUT ->
                InspirationCarouselViewHolder(view, inspirationCarouselListener)
            SearchLoadingMoreViewHolder.LAYOUT -> SearchLoadingMoreViewHolder(view)
            RecommendationTitleViewHolder.LAYOUT -> RecommendationTitleViewHolder(view)
            RecommendationItemViewHolder.LAYOUT ->
                RecommendationItemViewHolder(view, recommendationListener)
            BannedProductsEmptySearchViewHolder.LAYOUT -> BannedProductsEmptySearchViewHolder(view)
            BannedProductsTickerViewHolder.LAYOUT -> BannedProductsTickerViewHolder(view)
            BroadMatchViewHolder.LAYOUT -> BroadMatchViewHolder(view, broadMatchListener)
            SmallGridInspirationCardViewHolder.LAYOUT ->
                SmallGridInspirationCardViewHolder(view, inspirationCardListener)
            BigGridInspirationCardViewHolder.LAYOUT ->
                BigGridInspirationCardViewHolder(view, inspirationCardListener)
            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view)
            SearchProductTitleViewHolder.LAYOUT -> SearchProductTitleViewHolder(view)
            SearchInTokopediaViewHolder.LAYOUT ->
                SearchInTokopediaViewHolder(view, searchInTokopediaListener)
            SearchProductCountViewHolder.LAYOUT ->
                SearchProductCountViewHolder(view, searchNavigationListener)
            SearchProductTopAdsImageViewHolder.LAYOUT ->
                SearchProductTopAdsImageViewHolder(view, topAdsImageViewListener)
            ChooseAddressViewHolder.LAYOUT ->
                ChooseAddressViewHolder(view, chooseAddressListener, searchNavigationListener)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view, bannerListener)
            LastFilterViewHolder.LAYOUT -> LastFilterViewHolder(view, lastFilterListener)
            InspirationSizeViewHolder.LAYOUT -> InspirationSizeViewHolder(view, inspirationSizeListener)

            else -> super.createViewHolder(view, type)
        }
    }
}