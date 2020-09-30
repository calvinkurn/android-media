package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*

interface HistorySearchListener {
    fun onClearSearchItem(keyword: String, adapterPosition: Int)
    fun onClearAllSearch()
    fun onHistoryItemClicked(keyword: String)
}

interface FaqSearchListener {
    fun onFaqItemClicked(data: FaqSellerSearchUiModel, position: Int)
    fun onFaqMoreClicked(element: TitleHasMoreSellerSearchUiModel, position: Int)
}

interface NavigationSearchListener {
    fun onNavigationItemClicked(data: NavigationSellerSearchUiModel, position: Int)
}

interface OrderSearchListener {
    fun onOrderItemClicked(data: OrderSellerSearchUiModel, position: Int)
    fun onOrderMoreClicked(element: TitleHasMoreSellerSearchUiModel, position: Int)
}

interface ProductSearchListener {
    fun onProductItemClicked(data: ProductSellerSearchUiModel, position: Int)
    fun onProductMoreClicked(element: TitleHasMoreSellerSearchUiModel, position: Int)
}

interface HistoryViewUpdateListener {
    fun setUserIdFromFragment(userId: String)
    fun showHistoryView()
    fun dropKeyboardHistory()
    fun setKeywordSearchBarView(keyword: String)
}

interface SuggestionViewUpdateListener {
    fun showSuggestionView()
    fun dropKeyboardSuggestion()
}