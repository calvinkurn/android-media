package com.tokopedia.autocomplete.suggestion.title

import com.tokopedia.autocomplete.suggestion.SuggestionItem

fun SuggestionItem.convertToTitleHeader(): SuggestionTitleViewModel {
    return SuggestionTitleViewModel(title)
}