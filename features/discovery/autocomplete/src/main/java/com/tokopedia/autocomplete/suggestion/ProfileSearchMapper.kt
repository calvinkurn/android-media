package com.tokopedia.autocomplete.suggestion

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.viewmodel.ProfileSearch
import java.util.ArrayList

fun SearchData.convertProfileSearchToVisitableList(searchTerm: String): MutableList<Visitable<*>> {
    val list = ArrayList<Visitable<*>>()
    var positionOfProfileSearch = 1
    for (item in this.items) {
        val model = ProfileSearch()
        model.keyword = item.keyword
        model.url = item.url
        model.applink = item.applink
        model.imageUrl = item.imageURI
        model.peopleId = item.itemId
        model.affiliateUserName = if (!TextUtils.isEmpty(item.affiliateUserName)) item.affiliateUserName else ""
        model.isKOL = item.isKOL
        model.postCount = item.postCount
        model.searchTerm = searchTerm
        model.positionOfType = positionOfProfileSearch
        list.add(model)

        positionOfProfileSearch += 1
    }
    return list
}