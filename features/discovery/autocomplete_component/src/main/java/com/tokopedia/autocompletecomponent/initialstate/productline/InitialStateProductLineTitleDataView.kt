package com.tokopedia.autocompletecomponent.initialstate.productline

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory

class InitialStateProductLineTitleDataView(val title: String = "") : Visitable<InitialStateTypeFactory> {

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
