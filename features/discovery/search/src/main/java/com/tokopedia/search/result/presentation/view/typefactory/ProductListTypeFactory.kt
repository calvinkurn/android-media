package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
import com.tokopedia.search.result.product.emptystate.EmptyStateFilterDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.size.InspirationSizeDataView
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.violation.ViolationDataView

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
    fun type(emptySearchProductDataView: EmptyStateKeywordDataView): Int
    fun type(emptySearchFilterDataView: EmptyStateFilterDataView): Int
    fun type(broadMatchDataView: BroadMatchDataView): Int
    fun type(inspirationCardDataView: InspirationCardDataView): Int
    fun type(searchProductTitleDataView: SearchProductTitleDataView): Int
    fun type(separatorDataView: SeparatorDataView): Int
    fun type(searchInTokopediaDataView: SearchInTokopediaDataView): Int
    fun type(searchProductCountDataView: SearchProductCountDataView): Int
    fun type(searchProductTopAdsImageDataView: SearchProductTopAdsImageDataView): Int
    fun type(chooseAddressDataView: ChooseAddressDataView): Int
    fun type(bannerDataView: BannerDataView): Int
    fun type(lastFilterDataView: LastFilterDataView): Int
    fun type(sizeDataView: InspirationSizeDataView): Int
    fun type(violationView: ViolationDataView) : Int
    var recyclerViewItem: Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}