package com.tokopedia.autocomplete.suggestion

fun SuggestionItem.convertSuggestionItemToDoubleLineVisitableList(searchTerm: String, position: Int = -1): SuggestionDoubleLineViewModel {
    val item = SuggestionDoubleLineViewModel()
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
    item.searchTerm = searchTerm
    item.position = position
    return item
}