package com.tokopedia.autocomplete.initialstate

interface InitialStateItemClickListener {
    fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch)

    fun onDeleteAllRecentSearch()

    fun onRefreshPopularSearch(id: String)

    fun onItemClicked(applink: String, webUrl: String)

    fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

    fun onRefreshDynamicSection(id: String)
}