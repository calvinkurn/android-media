package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class TitleHeaderSellerSearchUiModel(val title: String): BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }
}