package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

interface RecentSearchListener {

    fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch)

    fun onDeleteAllRecentSearch()

    fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch)

    fun onRecentSearchSeeMoreClicked()
}