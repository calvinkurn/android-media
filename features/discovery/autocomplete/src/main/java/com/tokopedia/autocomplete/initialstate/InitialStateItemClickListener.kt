package com.tokopedia.autocomplete.initialstate

interface InitialStateItemClickListener {
    fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch)

    fun onDeleteAllRecentSearch()

    fun onRefreshPopularSearch()

    fun onItemClicked(applink: String, webUrl: String)

    fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)
}