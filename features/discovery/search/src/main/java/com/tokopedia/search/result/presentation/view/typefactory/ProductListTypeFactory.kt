package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.product.ads.AdsLowOrganicTitleDataView
import com.tokopedia.search.result.product.banned.BannedProductsEmptySearchDataView
import com.tokopedia.search.result.product.banner.BannerDataView
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateFilterDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateKeywordDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundleDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcDataView
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcDataView
import com.tokopedia.search.result.product.inspirationwidget.card.InspirationCardDataView
import com.tokopedia.search.result.product.inspirationwidget.filter.InspirationFilterDataView
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproducttitle.InspirationProductTitleDataView
import com.tokopedia.search.result.product.separator.VerticalSeparatorDataView
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import com.tokopedia.search.result.product.violation.ViolationDataView

interface ProductListTypeFactory {
    fun type(productItem: ProductItemDataView): Int
    fun type(cpmDataView: CpmDataView): Int
    fun type(tickerDataView: TickerDataView): Int
    fun type(suggestionDataView: SuggestionDataView): Int
    fun type(globalNavDataView: GlobalNavDataView): Int
    fun type(inspirationCarouselDataView: InspirationCarouselDataView): Int
    fun type(inspirationCarouselDataView: InspirationCarouselVideoDataView): Int
    fun type(titleViewModel: RecommendationTitleDataView): Int
    fun type(recommendationItemDataView: RecommendationItemDataView): Int
    fun type(bannedProductsEmptySearchDataView: BannedProductsEmptySearchDataView): Int
    fun type(emptySearchProductDataView: EmptyStateKeywordDataView): Int
    fun type(emptySearchFilterDataView: EmptyStateFilterDataView): Int
    fun type(broadMatchDataView: BroadMatchDataView): Int
    fun type(inspirationCardDataView: InspirationCardDataView): Int
    fun type(searchProductTitleDataView: SearchProductTitleDataView): Int
    fun type(searchInTokopediaDataView: SearchInTokopediaDataView): Int
    fun type(searchProductTopAdsImageDataView: SearchProductTopAdsImageDataView): Int
    fun type(chooseAddressDataView: ChooseAddressDataView): Int
    fun type(bannerDataView: BannerDataView): Int
    fun type(lastFilterDataView: LastFilterDataView): Int
    fun type(sizeDataView: InspirationFilterDataView): Int
    fun type(violationView: ViolationDataView) : Int
    fun type(inspirationProductBundleDataView: InspirationProductBundleDataView) : Int
    fun type(sameSessionRecommendationDataView: SameSessionRecommendationDataView) : Int
    fun type(inspirationListAtcDataView: InspirationListAtcDataView): Int
    fun type(postAtcCarouselDataView : InspirationListPostAtcDataView) : Int
    fun type(adsLowOrganicTitleDataView: AdsLowOrganicTitleDataView): Int
    fun type(inspirationKeywordCardView: InspirationKeywordCardView): Int
    fun type(inspirationProductCardView: InspirationProductItemDataView): Int
    fun type(inspirationCarouselSeamlessProductTitle: InspirationProductTitleDataView): Int
    fun type(separatorDataView: VerticalSeparatorDataView): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
