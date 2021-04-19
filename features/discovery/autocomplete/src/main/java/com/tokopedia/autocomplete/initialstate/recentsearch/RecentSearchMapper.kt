package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItem
import java.util.*

fun List<InitialStateItem>.convertToRecentSearchDataView(): RecentSearchDataView {
    val childList = ArrayList<BaseItemInitialStateSearch>()
    for (item in this) {
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
    return RecentSearchDataView(childList)
}