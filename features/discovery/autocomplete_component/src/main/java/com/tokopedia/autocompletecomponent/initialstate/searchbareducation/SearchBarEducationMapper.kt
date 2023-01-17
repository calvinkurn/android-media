package com.tokopedia.autocompletecomponent.initialstate.searchbareducation

import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData

fun InitialStateData.convertToSearchBarEducationDataView(
    dimension90: String,
    keyword: String,
): SearchBarEducationDataView? {
    val item = this.items.firstOrNull()?.let { item ->
        BaseItemInitialStateSearch(
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
            type = item.type,
            position = 1,
            componentId = item.componentId,
            trackingOption = trackingOption,
            keyword = keyword,
        )
    }

    return if (item != null) SearchBarEducationDataView(header, labelAction, item)
    else null
}