package com.tokopedia.autocomplete.suggestion.singleline

import com.tokopedia.autocomplete.suggestion.SuggestionItem

fun SuggestionItem.convertSuggestionItemToSingleLineVisitableList(searchTerm: String, position: Int): SuggestionSingleLineViewModel {
    val item = SuggestionSingleLineViewModel()
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
    item.searchTerm = searchTerm
    item.position = position
    return item
}