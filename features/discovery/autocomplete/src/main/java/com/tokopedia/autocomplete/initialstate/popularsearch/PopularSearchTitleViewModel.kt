package com.tokopedia.autocomplete.initialstate.popularsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class PopularSearchTitleViewModel(val isVisible: Boolean = false) : Visitable<InitialStateTypeFactory> {

    var title: String = ""

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
