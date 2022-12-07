package com.tokopedia.autocompletecomponent.initialstate.searchbareducation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory

class SearchBarEducationDataView(
    var header: String = "",
    var labelAction: String = "",
    var item: BaseItemInitialStateSearch = BaseItemInitialStateSearch()
): Visitable<InitialStateTypeFactory> {

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}