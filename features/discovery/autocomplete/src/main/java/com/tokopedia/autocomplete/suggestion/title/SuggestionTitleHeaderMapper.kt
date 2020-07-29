package com.tokopedia.autocomplete.suggestion.title

import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToTitleHeader(): SuggestionTitleViewModel {
    return SuggestionTitleViewModel(title)
}