package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.*

interface ProductListTypeFactory {
    fun type(productItem: ProductItemDataView): Int
    fun type(cpmDataView: CpmDataView): Int
    fun type(tickerDataView: TickerDataView): Int
    fun type(suggestionDataView: SuggestionDataView): Int
    fun type(globalNavDataView: GlobalNavDataView): Int
    fun type(inspirationCarouselDataView: InspirationCarouselDataView): Int
    fun type(titleViewModel: RecommendationTitleDataView): Int
    fun type(recommendationItemDataView: RecommendationItemDataView): Int
    fun type(bannedProductsEmptySearchDataView: BannedProductsEmptySearchDataView): Int
    fun type(bannedProductsTickerDataView: BannedProductsTickerDataView): Int
    fun type(emptySearchProductDataView: EmptySearchProductDataView): Int
    fun type(broadMatchDataView: BroadMatchDataView): Int
    fun type(inspirationCardDataView: InspirationCardDataView): Int
    fun type(searchProductTitleDataView: SearchProductTitleDataView): Int
    fun type(separatorDataView: SeparatorDataView): Int
    fun type(searchInTokopediaDataView: SearchInTokopediaDataView): Int
    fun type(searchProductCountDataView: SearchProductCountDataView): Int
    fun type(searchProductTopAdsImageDataView: SearchProductTopAdsImageDataView): Int
    fun type(chooseAddressDataView: ChooseAddressDataView): Int
    fun type(bannerDataView: BannerDataView): Int
    var recyclerViewItem: Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}