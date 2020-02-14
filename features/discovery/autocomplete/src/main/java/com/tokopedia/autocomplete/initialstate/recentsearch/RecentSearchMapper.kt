package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.newfiles.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.newfiles.InitialStateData
import java.util.ArrayList

fun InitialStateData.convertRecentSearchToVisitableList(): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    val recentSearch = RecentSearchViewModel()
    val childList = ArrayList<BaseItemInitialStateSearch>()
    for (item in this.items) {
        val model = BaseItemInitialStateSearch()
        model.template = item.template
        model.imageUrl = item.imageUrl
        model.applink = item.applink
        model.url = item.url
        model.title = item.title
        model.subtitle = item.subtitle
        model.iconTitle = item.iconTitle
        model.iconSubtitle = item.iconSubtitle
        model.label = item.label
        model.shortcutUrl = item.shortcutUrl
        model.shortcutImage = item.shortcutImage
        model.productId = item.itemId
        model.productPrice = item.price
        childList.add(model)
    }
    recentSearch.list = childList
    list.add(recentSearch)
    return list
}