package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToDoubleLineVisitableList(searchTerm: String, position: Int, dimension90: String): SuggestionDoubleLineDataDataView {
    val item = SuggestionDoubleLineDataDataView()
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
    item.dimension90 = dimension90
    item.trackingCode = this.tracking.code
    return item
}