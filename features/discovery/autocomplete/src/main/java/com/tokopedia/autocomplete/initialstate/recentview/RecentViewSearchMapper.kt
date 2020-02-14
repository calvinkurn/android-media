package com.tokopedia.autocomplete.initialstate.recentview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.newfiles.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.newfiles.InitialStateData
import java.util.*

fun InitialStateData.convertRecentViewSearchToVisitableList(): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    val recentViewSearch = RecentViewSearchViewModel()
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
    recentViewSearch.list = childList
    list.add(recentViewSearch)
    return list
}