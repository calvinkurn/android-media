package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestion
import com.tokopedia.autocompletecomponent.suggestion.convertToBaseSuggestionShopAds
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem
import com.tokopedia.topads.sdk.domain.model.CpmData

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

fun SuggestionItem.convertToDoubleLineShopAds(
    searchTerm: String,
    position: Int,
    dimension90: String,
    cpmData: CpmData,
): SuggestionDoubleLineDataDataView {
    return SuggestionDoubleLineDataDataView(
        data = convertToBaseSuggestionShopAds(
            searchTerm,
            position,
            dimension90,
            cpmData,
        )
    )
}