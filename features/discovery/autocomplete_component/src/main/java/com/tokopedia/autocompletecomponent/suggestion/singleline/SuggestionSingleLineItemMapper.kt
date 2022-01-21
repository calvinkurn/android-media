package com.tokopedia.autocompletecomponent.suggestion.singleline

import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestion
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSingleLineVisitableList(
    searchTerm: String,
    position: Int,
    dimension90: String,
): SuggestionSingleLineDataDataView {
    return SuggestionSingleLineDataDataView(
        data = convertToBaseSuggestion(
            searchTerm,
            position,
            dimension90,
        )
    )
}