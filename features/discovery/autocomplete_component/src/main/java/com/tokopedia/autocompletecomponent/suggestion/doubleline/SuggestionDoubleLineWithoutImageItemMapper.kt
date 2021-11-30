package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestion
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToDoubleLineWithoutImageVisitableList(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionDoubleLineWithoutImageDataDataView {
    return SuggestionDoubleLineWithoutImageDataDataView(
        data = convertToBaseSuggestion(
            searchTerm,
            position,
            dimension90,
        )
    )
}