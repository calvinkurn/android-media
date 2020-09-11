package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreViewModel

interface InitialStateItemClickListener {
    fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch)

    fun onDeleteAllRecentSearch()

    fun onRefreshPopularSearch()

    fun onItemClicked(applink: String, webUrl: String)

    fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch, adapterPosition: Int)

    fun onRecentSearchSeeMoreClicked(item: RecentSearchSeeMoreViewModel)
}