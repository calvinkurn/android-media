package com.tokopedia.autocomplete.initialstate.popularsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateData
import java.util.ArrayList

fun InitialStateData.convertPopularSearchToVisitableList(): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    val popularSearch = PopularSearchViewModel()
    val childList = ArrayList<BaseItemInitialStateSearch>()
    for (item in this.items) {
        val model = BaseItemInitialStateSearch()
        model.template = item.template
        model.imageUrl = item.imageUrl
        model.applink = item.applink
        model.url = item.url
        model.title = item.title
        childList.add(model)
    }
    popularSearch.list = childList
    list.add(popularSearch)
    return list
}