package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.*
import java.util.ArrayList

fun SearchData.convertRecentViewSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    val recentViewSearch = RecentViewSearch()
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
        model.imageUrl = item.imageURI
        model.productId = item.itemId
        model.productPrice = item.price
        model.isOfficial = item.isOfficial
        childList.add(model)
    }
    recentViewSearch.list = childList
    list.add(recentViewSearch)
    return list
}