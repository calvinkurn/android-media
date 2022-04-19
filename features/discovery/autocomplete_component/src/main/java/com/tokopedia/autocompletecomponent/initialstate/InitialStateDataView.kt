package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import java.util.*

class InitialStateDataView{

    val list = ArrayList<InitialStateData>()

    fun addList(visitable: InitialStateData) {
        this.list.add(visitable)
    }
}
