package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateData
import java.util.ArrayList

fun InitialStateData.convertRecentSearchToVisitableList(): MutableList<Visitable<*>> {
    val childList = ArrayList<BaseItemInitialStateSearch>()
    for (item in this.items) {
        val model = BaseItemInitialStateSearch(
                template = item.template,
                imageUrl = item.imageUrl,
                applink =  item.applink,
                url = item.url,
                title = item.title,
                subtitle = item.subtitle,
                iconTitle = item.iconTitle,
                iconSubtitle = item.iconSubtitle,
                label = item.label,
                labelType = item.labelType,
                shortcutImage = item.shortcutImage,
                productId = item.itemId,
                type = item.type
        )
        childList.add(model)
    }
    return arrayListOf(RecentSearchViewModel(childList))
}