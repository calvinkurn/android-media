package com.tokopedia.autocomplete.suggestion.doubleline

import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToDoubleLineWithoutImageVisitableList(searchTerm: String, position: Int): SuggestionDoubleLineWithoutImageDataDataView {
    val item = SuggestionDoubleLineWithoutImageDataDataView()
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
    item.label = this.label
    item.labelType = this.labelType
    item.urlTracker = this.urlTracker
    item.searchTerm = searchTerm
    item.position = position
    return item
}