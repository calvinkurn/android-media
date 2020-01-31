package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.*
import java.util.ArrayList

fun SearchData.convertPopularSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    val popularSearch = PopularSearch()
    val childList = ArrayList<BaseItemAutoCompleteSearch>()
    for (item in this.items) {
        val model = BaseItemAutoCompleteSearch()
        model.eventId = this.id
        model.eventName = this.name
        model.applink = item.applink
        model.recom = item.recom
        model.url = item.url
        model.keyword = item.keyword
        model.searchTerm = searchTerm
        model.isOfficial = item.isOfficial
        childList.add(model)
    }
    popularSearch.list = childList
    list.add(popularSearch)
    return list
}