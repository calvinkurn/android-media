package com.tokopedia.autocomplete.initialstate.popularsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class PopularSearchDataView(
        val featureId: String = "",
        var list: List<BaseItemInitialStateSearch> = listOf()
): Visitable<InitialStateTypeFactory>{

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }

}