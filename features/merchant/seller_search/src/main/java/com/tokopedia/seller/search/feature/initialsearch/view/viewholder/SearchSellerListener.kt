package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

interface HistorySearchListener {
    fun onClearSearchItem(keyword: String)
    fun onClearAllSearch()
    fun onHistoryItemClicked(appUrl: String)
}

interface NavigationSearchListener {
    fun onNavigationItemClicked(appUrl: String)
}

interface OrderSearchListener {
    fun onOrderItemClicked(appUrl: String)
}

interface ProductSearchListener {
    fun onProductItemClicked(appUrl: String)
}

interface FilterSearchListener {
    fun onFilterItemClicked(title: String, chipType: String, position: Int)
}