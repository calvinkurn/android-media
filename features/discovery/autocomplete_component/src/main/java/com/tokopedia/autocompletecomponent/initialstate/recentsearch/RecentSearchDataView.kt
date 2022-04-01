package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory

class RecentSearchDataView(
    var list: MutableList<BaseItemInitialStateSearch> = mutableListOf(),
    val trackingOption: Int = 0,
): Visitable<InitialStateTypeFactory>{

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}