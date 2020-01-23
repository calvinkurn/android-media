package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.DigitalSearch
import java.util.ArrayList

fun SearchData.convertDigitalSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    for (item in this.items) {
        val model = DigitalSearch()
        model.eventId = this.id
        model.eventName = this.name
        model.applink = item.applink
        model.recom = item.recom
        model.url = item.url
        model.imageUrl = item.imageURI
        model.keyword = item.keyword
        model.searchTerm = searchTerm
        model.isOfficial = item.isOfficial
        list.add(model)
    }
    return list
}