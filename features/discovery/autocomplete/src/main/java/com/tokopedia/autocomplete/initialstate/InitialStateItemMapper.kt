package com.tokopedia.autocomplete.initialstate

fun List<InitialStateItem>.convertToBaseItemInitialStateSearch(): List<BaseItemInitialStateSearch> {
    val childList = ArrayList<BaseItemInitialStateSearch>()
    for (item in this) {
        val model = BaseItemInitialStateSearch(
                template = item.template,
                imageUrl = item.imageUrl,
                applink = item.applink,
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
    return childList
}