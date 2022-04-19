package com.tokopedia.autocompletecomponent.initialstate.productline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateAdapterTypeFactory

class InitialStateProductListDataView(
    val list: MutableList<BaseItemInitialStateSearch> = mutableListOf()
): Visitable<InitialStateAdapterTypeFactory> {

    override fun type(typeFactory: InitialStateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}