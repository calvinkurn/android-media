package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.InCategorySearch
import java.util.ArrayList

fun SearchData.convertInCategorySearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    for (item in this.items) {
        val model = InCategorySearch()
        model.eventId = this.id
        model.eventName = this.name
        model.searchTerm = searchTerm
        model.keyword = item.keyword
        model.url = item.url
        model.applink = item.applink
        model.recom = item.recom
        model.categoryId = item.sc
        model.isOfficial = item.isOfficial
        list.add(model)
    }
    return list
}