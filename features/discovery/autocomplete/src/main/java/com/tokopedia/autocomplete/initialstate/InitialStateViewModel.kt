package com.tokopedia.autocomplete.initialstate

import java.util.*

class InitialStateViewModel{

    val list = ArrayList<InitialStateData>()
    var searchTerm = ""

    fun addList(visitable: InitialStateData) {
        this.list.add(visitable)
    }
}
