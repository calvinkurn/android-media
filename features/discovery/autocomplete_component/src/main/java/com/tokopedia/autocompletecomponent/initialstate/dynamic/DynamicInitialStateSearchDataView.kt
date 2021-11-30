package com.tokopedia.autocompletecomponent.initialstate.dynamic

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory

class DynamicInitialStateSearchDataView(
        val featureId: String = "",
        var list: List<BaseItemInitialStateSearch> = listOf()
): Visitable<InitialStateTypeFactory> {

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }

}