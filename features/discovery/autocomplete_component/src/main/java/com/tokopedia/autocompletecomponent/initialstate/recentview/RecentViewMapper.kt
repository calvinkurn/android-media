package com.tokopedia.autocompletecomponent.initialstate.recentview

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData

fun InitialStateData.convertToRecentViewDataView(
    dimension90: String,
    keyword: String,
): RecentViewDataView {
    val childList = mutableListOf<BaseItemInitialStateSearch>()
    var position = 1
    for (item in this.items) {
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
            dimension90 = dimension90,
            position = position,
            componentId = item.componentId,
            trackingOption = trackingOption,
            keyword = keyword,
        )
        childList.add(model)
        position++
    }
    return RecentViewDataView(childList)
}