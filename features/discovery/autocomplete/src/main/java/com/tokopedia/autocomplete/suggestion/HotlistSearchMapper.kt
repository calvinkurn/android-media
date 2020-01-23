package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.HotlistSearch
import java.util.ArrayList

fun SearchData.convertHotlistSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    for (item in this.items) {
        val model = HotlistSearch()
        model.eventId = this.id
        model.eventName = this.name
        model.applink = item.applink
        model.url = item.url
        model.keyword = item.keyword
        model.searchTerm = searchTerm
        model.imageUrl = item.imageURI
        model.searchTerm = searchTerm
        model.categoryId = item.sc
        list.add(model)
    }
    return list
}