package com.tokopedia.seller.search.feature.suggestion.view.model

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter

class SellerSearchNoResultUiModel : BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }

    override fun getUniquePosition(): Int {
        return SELLER_SEARCH_NO_RESULT_UI_MODEL.hashCode()
    }

    companion object {
        const val SELLER_SEARCH_NO_RESULT_UI_MODEL = "SellerSearchNoResultUiModel"
    }
}
