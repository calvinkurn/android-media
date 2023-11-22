package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

class DividerSellerSearchUiModel(
    val position: Int
) : BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }

    override fun getUniquePosition(): Int {
        return DIVIDER_SELLER_SEARCH_UI_MODEL.hashCode() + position.hashCode()
    }

    companion object {
        const val DIVIDER_SELLER_SEARCH_UI_MODEL = "DividerSellerSearchUiModel"
    }
}
