package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel

interface HistorySearchListener {
    fun onClearSearchItem(keyword: String, adapterPosition: Int)
    fun onClearAllSearch()
    fun onHistoryItemClicked(keyword: String)
}

interface FaqSearchListener {
    fun onFaqItemClicked(data: ItemSellerSearchUiModel, position: Int)
    fun onFaqMoreClicked(element: SellerSearchUiModel, position: Int)
}

interface NavigationSearchListener {
    fun onNavigationItemClicked(data: ItemSellerSearchUiModel, position: Int)
}

interface OrderSearchListener {
    fun onOrderItemClicked(data: ItemSellerSearchUiModel, position: Int)
    fun onOrderMoreClicked(element: SellerSearchUiModel, position: Int)
}

interface ProductSearchListener {
    fun onProductItemClicked(data: ItemSellerSearchUiModel, position: Int)
    fun onProductMoreClicked(element: SellerSearchUiModel, position: Int)
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