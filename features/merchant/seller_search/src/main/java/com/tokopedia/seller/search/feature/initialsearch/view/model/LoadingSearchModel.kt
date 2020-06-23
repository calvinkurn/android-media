package com.tokopedia.seller.search.feature.initialsearch.view.model

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter

class LoadingSearchModel : BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }
}