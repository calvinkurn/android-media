package com.tokopedia.autocompletecomponent.initialstate.recentview

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

interface RecentViewListener {

    fun onRecentViewClicked(item: BaseItemInitialStateSearch)
}