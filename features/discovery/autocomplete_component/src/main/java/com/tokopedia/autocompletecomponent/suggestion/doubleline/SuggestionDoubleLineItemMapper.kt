package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestion
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToDoubleLineVisitableList(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionDoubleLineDataDataView {
    return SuggestionDoubleLineDataDataView(
        data = convertToBaseSuggestion(
            searchTerm,
            position,
            dimension90,
        )
    )
}