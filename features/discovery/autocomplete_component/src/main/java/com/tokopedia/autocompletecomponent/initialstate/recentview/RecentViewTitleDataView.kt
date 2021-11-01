package com.tokopedia.autocompletecomponent.initialstate.recentview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory

class RecentViewTitleDataView(val title: String = "") : Visitable<InitialStateTypeFactory> {

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
