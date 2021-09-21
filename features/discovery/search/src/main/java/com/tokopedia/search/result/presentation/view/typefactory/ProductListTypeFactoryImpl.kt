package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.search.result.presentation.view.adapter.viewholder.common.SearchLoadingMoreViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.*
import com.tokopedia.search.result.presentation.view.listener.*
import com.tokopedia.topads.sdk.base.Config

class ProductListTypeFactoryImpl(private val productListener: ProductListener,
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
                                 private val topAdsConfig: Config,
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
            SearchConstant.RecyclerView.VIEW_LIST -> ListProductItemViewHolder.LAYOUT
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> BigGridProductItemViewHolder.LAYOUT
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> SmallGridProductItemViewHolder.LAYOUT
            else -> SmallGridProductItemViewHolder.LAYOUT
        }
    }

    override fun type(emptySearchProductDataView: EmptySearchProductDataView): Int {
        return ProductEmptySearchViewHolder.LAYOUT
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
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> BigGridInspirationCardViewHolder.LAYOUT
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> SmallGridInspirationCardViewHolder.LAYOUT
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

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ListProductItemViewHolder.LAYOUT -> ListProductItemViewHolder(view, productListener)

            SmallGridProductItemViewHolder.LAYOUT -> SmallGridProductItemViewHolder(view, productListener)

            BigGridProductItemViewHolder.LAYOUT -> BigGridProductItemViewHolder(view, productListener)

            CpmViewHolder.LAYOUT -> CpmViewHolder(view, bannerAdsListener)

            TickerViewHolder.LAYOUT -> TickerViewHolder(view, tickerListener)

            SuggestionViewHolder.LAYOUT -> SuggestionViewHolder(view, suggestionListener)

            ProductEmptySearchViewHolder.LAYOUT ->
                ProductEmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, topAdsConfig)

            GlobalNavViewHolder.LAYOUT -> GlobalNavViewHolder(view, globalNavListener)

            InspirationCarouselViewHolder.LAYOUT -> InspirationCarouselViewHolder(view, inspirationCarouselListener)

            SearchLoadingMoreViewHolder.LAYOUT -> SearchLoadingMoreViewHolder(view)

            RecommendationTitleViewHolder.LAYOUT -> RecommendationTitleViewHolder(view)

            RecommendationItemViewHolder.LAYOUT -> RecommendationItemViewHolder(view, recommendationListener)

            BannedProductsEmptySearchViewHolder.LAYOUT -> BannedProductsEmptySearchViewHolder(view)

            BannedProductsTickerViewHolder.LAYOUT -> BannedProductsTickerViewHolder(view)

            BroadMatchViewHolder.LAYOUT -> BroadMatchViewHolder(view, broadMatchListener)

            SmallGridInspirationCardViewHolder.LAYOUT ->
                SmallGridInspirationCardViewHolder(view, inspirationCardListener)

            BigGridInspirationCardViewHolder.LAYOUT ->
                BigGridInspirationCardViewHolder(view, inspirationCardListener)

            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view)

            SearchProductTitleViewHolder.LAYOUT -> SearchProductTitleViewHolder(view)

            SearchInTokopediaViewHolder.LAYOUT -> SearchInTokopediaViewHolder(view, searchInTokopediaListener)

            SearchProductCountViewHolder.LAYOUT -> SearchProductCountViewHolder(view, searchNavigationListener)

            SearchProductTopAdsImageViewHolder.LAYOUT -> SearchProductTopAdsImageViewHolder(view, topAdsImageViewListener)

            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(view, chooseAddressListener, searchNavigationListener)

            BannerViewHolder.LAYOUT -> BannerViewHolder(view, bannerListener)

            else -> super.createViewHolder(view, type)
        }
    }
}