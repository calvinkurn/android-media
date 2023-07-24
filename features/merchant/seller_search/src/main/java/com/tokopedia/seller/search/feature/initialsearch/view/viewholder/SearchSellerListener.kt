package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel

interface HistorySearchListener {
    fun onClearSearchItem(keyword: String, adapterPosition: Int)
    fun onClearAllSearch()
    fun onHistoryItemClicked(keyword: String)
    fun onHighlightItemClicked(data: ItemHighlightInitialSearchUiModel, position: Int)
}

interface FaqSearchListener {
    fun onFaqItemClicked(data: FaqSellerSearchUiModel, position: Int)
    fun onFaqMoreClicked(element: TitleHasMoreSellerSearchUiModel)
}

interface NavigationSearchListener {
    fun onNavigationItemClicked(data: NavigationSellerSearchUiModel, position: Int)
}

interface OrderSearchListener {
    fun onOrderItemClicked(data: OrderSellerSearchUiModel, position: Int)
    fun onOrderMoreClicked(element: TitleHasMoreSellerSearchUiModel)
}

interface ProductSearchListener {
    fun onProductItemClicked(data: ProductSellerSearchUiModel, position: Int)
    fun onProductMoreClicked(element: TitleHasMoreSellerSearchUiModel)
}

interface ArticleSearchListener {
    fun onArticleItemClicked(data: ArticleSellerSearchUiModel, position: Int)
    fun onArticleMoreClicked(element: TitleHasMoreSellerSearchUiModel)
}

interface HighlightSuggestionSearchListener {
    fun onHighlightItemClicked(data: ItemHighlightSuggestionSearchUiModel, position: Int)
}

interface HistoryViewUpdateListener {
    fun showHistoryView()
    fun setKeywordSearchBarView(keyword: String)
}

interface SuggestionViewUpdateListener {
    fun showSuggestionView()
    fun dropKeyboardSuggestion()
}
