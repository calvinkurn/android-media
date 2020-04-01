package com.tokopedia.autocomplete.suggestion

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.TopProfileSearch
import java.util.ArrayList

fun SearchData.convertTopProfileSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    var positionOfTopProfileSearch = 1

    for (item in this.items) {
        val model = TopProfileSearch()
        model.keyword = item.keyword
        model.url = item.url
        model.applink = item.applink
        model.imageUrl = item.imageURI
        model.peopleId = item.itemId
        model.affiliateUserName = if (!TextUtils.isEmpty(item.affiliateUserName)) item.affiliateUserName else ""
        model.isKOL = item.isKOL
        model.postCount = item.postCount
        model.searchTerm = searchTerm
        model.positionOfType = positionOfTopProfileSearch
        list.add(model)

        positionOfTopProfileSearch += 1
    }
    return list
}