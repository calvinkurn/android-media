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
    item.childItems = this.suggestionChildItems.convertToChildItems(searchTerm, dimension90)
    return item
}

private fun List<SuggestionChildItem>.convertToChildItems(searchTerm: String, dimension90: String): List<BaseSuggestionDataView.ChildItem> {
    val list = mutableListOf<BaseSuggestionDataView.ChildItem>()
    var position = 1
    for (item in this) {
        list.add(
                BaseSuggestionDataView.ChildItem(
                        template = item.template,
                        type = item.type,
                        applink = item.applink,
                        url = item.url,
                        title = item.title,
                        searchTerm = searchTerm,
                        dimension90 = dimension90,
                        position = position
                )
        )
        position++
    }
    return list
}