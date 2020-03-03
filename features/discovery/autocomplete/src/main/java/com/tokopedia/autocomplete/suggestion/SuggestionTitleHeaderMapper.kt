package com.tokopedia.autocomplete.suggestion

fun SuggestionItem.convertToTitleHeader(): SuggestionTitleViewModel {
    return SuggestionTitleViewModel(title)
}