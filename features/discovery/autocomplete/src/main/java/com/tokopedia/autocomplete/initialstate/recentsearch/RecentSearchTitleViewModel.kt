package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class RecentSearchTitleViewModel(val isVisible: Boolean = false) : Visitable<InitialStateTypeFactory> {

    var title: String = ""
    var labelAction: String = ""

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
