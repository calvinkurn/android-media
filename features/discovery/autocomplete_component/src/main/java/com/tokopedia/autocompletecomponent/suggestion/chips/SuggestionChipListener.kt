package com.tokopedia.autocompletecomponent.suggestion.chips

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView

interface SuggestionChipListener {

    fun onChipImpressed(item: BaseSuggestionDataView.ChildItem)

    fun onChipClicked(
        baseSuggestionDataView: BaseSuggestionDataView,
        childItem: BaseSuggestionDataView.ChildItem,
    )
}