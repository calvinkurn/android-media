package com.tokopedia.autocomplete.initialstate

import java.util.*

class InitialStateDataView{

    val list = ArrayList<InitialStateData>()

    fun addList(visitable: InitialStateData) {
        this.list.add(visitable)
    }
}
