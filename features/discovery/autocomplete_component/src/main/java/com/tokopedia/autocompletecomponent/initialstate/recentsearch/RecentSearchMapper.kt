package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateItem
import java.util.*

fun List<InitialStateItem>.convertToRecentSearchDataView(
    dimension90: String,
    trackingOption: Int,
    keyword: String,
): MutableList<BaseItemInitialStateSearch> {
    val childList = ArrayList<BaseItemInitialStateSearch>()
    var position = 1
    for (item in this) {
        val model = BaseItemInitialStateSearch(
            itemId = item.itemId,
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
            type = item.type,
            dimension90 = dimension90,
            position = position,
            componentId = item.componentId,
            trackingOption = trackingOption,
            keyword = keyword,
        )
        childList.add(model)
        position++
    }
    return childList
}