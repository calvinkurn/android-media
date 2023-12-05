package com.tokopedia.seller.search.feature.suggestion.view.model

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter

class LoadingSearchModel : BaseSuggestionSearchSeller {
    override fun getUniquePosition(): Int {
        return LOADING_SEARCH_MODEL.hashCode()
    }

    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val LOADING_SEARCH_MODEL = "LoadingSearchModel"
    }
}
