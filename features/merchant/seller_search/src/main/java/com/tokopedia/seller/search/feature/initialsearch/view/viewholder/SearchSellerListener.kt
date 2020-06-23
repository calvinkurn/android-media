package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel

interface HistorySearchListener {
    fun onClearSearchItem(keyword: String, adapterPosition: Int)
    fun onClearAllSearch()
    fun onHistoryItemClicked(appUrl: String)
}

interface NavigationSearchListener {
    fun onNavigationItemClicked(data: ItemSellerSearchUiModel, position: Int)
}

interface OrderSearchListener {
    fun onOrderItemClicked(data: ItemSellerSearchUiModel, position: Int)
}

interface ProductSearchListener {
    fun onProductItemClicked(data: ItemSellerSearchUiModel, position: Int)
}

interface FilterSearchListener {
    fun onFilterItemClicked(title: String, chipType: String, position: Int)
}

interface HistoryViewUpdateListener {
    fun showHistoryView()
    fun dropKeyboardHistory()
}

interface SuggestionViewUpdateListener {
    fun showSuggestionView()
    fun dropKeyboardSuggestion()
}