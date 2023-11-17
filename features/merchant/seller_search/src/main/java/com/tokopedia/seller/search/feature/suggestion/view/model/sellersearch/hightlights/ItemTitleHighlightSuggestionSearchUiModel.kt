package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class ItemTitleHighlightSuggestionSearchUiModel(
    val title: String = ""
) : BaseSuggestionSearchSeller {
    override fun getUniquePosition(): Int {
        return TITLE_HIGHLIGHT_SUGGESTION_SEARCH.hashCode() + title.hashCode()
    }

    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val TITLE_HIGHLIGHT_SUGGESTION_SEARCH = "TitleHighlightSuggestionSearch"
    }
}
