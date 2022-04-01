package com.tokopedia.autocompletecomponent.suggestion.productline

import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestion
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSuggestionProductLineDataView(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionProductLineDataDataView {
    return SuggestionProductLineDataDataView(
        data = convertToBaseSuggestion(
            searchTerm,
            position,
            dimension90
        )
    )
}