package com.tokopedia.autocompletecomponent.initialstate.popularsearch

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

interface PopularSearchListener {

    fun onRefreshPopularSearch(featureId: String)

    fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch)
}