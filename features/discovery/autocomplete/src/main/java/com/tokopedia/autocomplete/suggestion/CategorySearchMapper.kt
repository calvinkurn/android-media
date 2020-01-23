package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.BaseItemAutoCompleteSearch
import com.tokopedia.autocomplete.viewmodel.CategorySearch
import java.util.ArrayList

fun SearchData.convertCategorySearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    val categorySearch = CategorySearch()
    val childList = ArrayList<BaseItemAutoCompleteSearch>()
    for (item in this.items) {
        val model = BaseItemAutoCompleteSearch()
        model.eventId = this.id
        model.eventName = this.name
        model.applink = item.applink
        model.recom = item.recom
        model.url = item.url
        model.imageUrl = item.imageURI
        model.keyword = item.keyword
        model.categoryId = item.sc
        model.searchTerm = searchTerm
        model.isOfficial = item.isOfficial
        childList.add(model)
    }
    categorySearch.list = childList
    list.add(categorySearch)
    return list
}