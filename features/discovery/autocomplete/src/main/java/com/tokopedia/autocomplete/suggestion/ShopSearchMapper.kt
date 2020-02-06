package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.ShopSearch
import java.util.ArrayList

fun SearchData.convertShopSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    for (item in this.items) {
        val model = ShopSearch()
        model.eventId = this.id
        model.eventName = this.name
        model.searchTerm = searchTerm
        model.keyword = item.keyword
        model.url = item.url
        model.applink = item.applink
        model.imageUrl = item.imageURI
        model.setOfficial(item.isOfficial)
        model.location = item.location
        model.shopBadgeIconUrl = item.shopBadgeIconUrl
        list.add(model)
    }
    return list
}