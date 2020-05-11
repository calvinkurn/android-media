package com.tokopedia.autocomplete.initialstate

interface InitialStateItemClickListener {
    fun onDeleteRecentSearchItem(keyword: String)

    fun onDeleteAllRecentSearch()

    fun onRefreshPopularSearch()

    fun onItemClicked(applink: String, webUrl: String)
}