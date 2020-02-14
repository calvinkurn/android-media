package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.newfiles.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.newfiles.InitialStateTypeFactory

class RecentSearchViewModel(
        var list: List<BaseItemInitialStateSearch> = listOf()
): Visitable<InitialStateTypeFactory>{

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }

}