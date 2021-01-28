package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.HighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel

interface TypeFactoryInitialSearchAdapter {
    fun type(initialSearchUiModel: ItemInitialSearchUiModel): Int
    fun type(sellerSearchMinCharUiModel: SellerSearchMinCharUiModel): Int
    fun type(sellerSearchNoHistoryUiModel: SellerSearchNoHistoryUiModel): Int
    fun type(itemTitleInitialSearchUiModel: ItemTitleInitialSearchUiModel): Int
    fun type(itemTitleHighlightInitialSearchUiModel: ItemTitleHighlightInitialSearchUiModel): Int
    fun type(highlightInitialSearchUiModel: HighlightInitialSearchUiModel): Int
}

interface TypeFactorySuggestionSearchAdapter {
    fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int
    fun type(titleHeaderSellerSearchUiModel: TitleHeaderSellerSearchUiModel): Int
    fun type(orderSellerSearchUiModel: OrderSellerSearchUiModel): Int
    fun type(productSellerSearchUiModel: ProductSellerSearchUiModel): Int
    fun type(navigationSellerSearchUiModel: NavigationSellerSearchUiModel): Int
    fun type(faqSellerSearchUiModel: FaqSellerSearchUiModel): Int
    fun type(titleHasMoreSellerSearchUiModel: TitleHasMoreSellerSearchUiModel): Int
    fun type(dividerSellerSearchUiModel: DividerSellerSearchUiModel): Int
    fun type(loadingModel: LoadingSearchModel): Int
    fun type(itemTitleHighlightSuggestionSearchUiModel: ItemTitleHighlightSuggestionSearchUiModel): Int
    fun type(highlightSuggestionSearchUiModel: HighlightSuggestionSearchUiModel): Int
}
