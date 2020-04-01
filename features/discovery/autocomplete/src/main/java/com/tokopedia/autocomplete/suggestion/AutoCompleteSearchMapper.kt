package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.AutoCompleteSearch
import java.util.ArrayList

fun SearchData.convertAutoCompleteSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    for (item in this.items) {
        val model = AutoCompleteSearch()
        model.eventId = this.id
        model.eventName = this.name
        model.applink = item.applink
        model.url = item.url
        model.keyword = item.keyword
        model.searchTerm = searchTerm
        model.isOfficial = item.isOfficial
        list.add(model)
    }
    return list
}