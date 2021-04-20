package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class HighlightSuggestionSearchUiModel(
        var highlightSuggestionSearch: List<ItemHighlightSuggestionSearchUiModel> = listOf()
): BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }
}