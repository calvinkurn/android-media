package com.tokopedia.autocompletecomponent.initialstate.dynamic

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch

interface DynamicInitialStateListener {

    fun onRefreshDynamicSection(featureId: String)

    fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch)
}