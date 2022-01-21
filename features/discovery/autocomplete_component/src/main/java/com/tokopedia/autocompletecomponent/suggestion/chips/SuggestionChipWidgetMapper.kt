package com.tokopedia.autocompletecomponent.suggestion.chips

import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestion
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSuggestionChipWidgetDataView(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionChipWidgetDataView {
    return SuggestionChipWidgetDataView(
        data = convertToBaseSuggestion(
            searchTerm,
            position,
            dimension90,
        )
    )
}