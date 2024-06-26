package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class HighlightSuggestionSearchUiModel(
    var highlightSuggestionSearch: List<ItemHighlightSuggestionSearchUiModel> = listOf()
) : BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }

    override fun getUniquePosition(): Int {
        return highlightSuggestionSearch.hashCode() + HIGHLIGHT_SUGGESTION_SEARCH_UI_MODEL.hashCode()
    }

    companion object {
        const val HIGHLIGHT_SUGGESTION_SEARCH_UI_MODEL = "HighlightSuggestionSearchUiModel"
    }
}
