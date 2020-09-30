package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class RecentSearchViewModel(
    var list: MutableList<BaseItemInitialStateSearch> = mutableListOf(),
    val position: Int = -1
): Visitable<InitialStateTypeFactory>{

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}