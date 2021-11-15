package com.tokopedia.autocompletecomponent.suggestion.title

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToTitleHeader(): SuggestionTitleDataView {
    return SuggestionTitleDataView(title)
}