package com.tokopedia.autocomplete.suggestion.chips

import com.tokopedia.autocomplete.suggestion.BaseSuggestionDataView
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionChildItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSuggestionChipWidgetDataView(searchTerm: String, position: Int, dimension90: String): SuggestionChipWidgetDataView {
    val item = SuggestionChipWidgetDataView()
    item.template = this.template
    item.type = this.type
    item.applink = this.applink
    item.url = this.url
    item.title = this.title
    item.subtitle = this.subtitle
    item.iconTitle = this.iconTitle
    item.iconSubtitle = this.iconSubtitle
    item.shortcutUrl = this.shortcutUrl
    item.shortcutImage = this.shortcutImage
    item.imageUrl = this.imageUrl
    item.urlTracker = this.urlTracker
    item.searchTerm = searchTerm
    item.position = position
    item.trackingCode = this.tracking.code
    item.discountPercentage = this.discountPercentage
    item.originalPrice = this.originalPrice
    item.dimension90 = dimension90
    item.childItems = this.suggestionChildItems.convertToChildItems()
    return item
}

private fun List<SuggestionChildItem>.convertToChildItems(): List<BaseSuggestionDataView.ChildItem> {
    val list = mutableListOf<BaseSuggestionDataView.ChildItem>()
    for (item in this) {
        list.add(
                BaseSuggestionDataView.ChildItem(
                        template = item.template,
                        type = item.type,
                        applink = item.applink,
                        url = item.url,
                        title = item.title
                )
        )
    }
    return list
}