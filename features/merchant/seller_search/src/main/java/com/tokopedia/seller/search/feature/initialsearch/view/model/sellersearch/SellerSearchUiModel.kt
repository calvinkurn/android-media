package com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseSuggestionSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter

data class SellerSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var count: Int? = 0,
        var titleList: List<String> = listOf(),
        var sellerSearchList: List<ItemSellerSearchUiModel> = listOf()
): BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }
}
