package com.tokopedia.autocomplete.suggestion.title

import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToTitleHeader(): SuggestionTitleDataView {
    return SuggestionTitleDataView(title)
}