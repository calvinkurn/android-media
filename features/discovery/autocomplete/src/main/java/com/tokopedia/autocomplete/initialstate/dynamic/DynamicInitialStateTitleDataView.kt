package com.tokopedia.autocomplete.initialstate.dynamic

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class DynamicInitialStateTitleDataView(
        val featureId: String = "",
        val title: String = "",
        val labelAction: String = ""
): Visitable<InitialStateTypeFactory> {

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
