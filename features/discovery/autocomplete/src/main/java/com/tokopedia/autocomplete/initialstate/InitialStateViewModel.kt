package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.HostAutoCompleteTypeFactory
import java.util.*

class InitialStateViewModel : Visitable<HostAutoCompleteTypeFactory> {

    val list = ArrayList<InitialStateData>()
    var searchTerm = ""

    fun addList(visitable: InitialStateData) {
        this.list.add(visitable)
    }

    override fun type(typeFactory: HostAutoCompleteTypeFactory): Int {
        return typeFactory.type(this)
    }
}
