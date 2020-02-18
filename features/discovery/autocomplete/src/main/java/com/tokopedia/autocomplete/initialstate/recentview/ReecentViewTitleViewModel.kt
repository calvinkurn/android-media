package com.tokopedia.autocomplete.initialstate.recentview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class ReecentViewTitleViewModel : Visitable<InitialStateTypeFactory> {

    var title: String = ""

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
