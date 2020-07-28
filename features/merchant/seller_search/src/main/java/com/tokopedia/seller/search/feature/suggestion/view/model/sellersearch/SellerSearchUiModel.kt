package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class SellerSearchUiModel(
        val id: String? = "",
        val hasMore: Boolean = false,
        val title: String? = "",
        val count: Int? = 0,
        val actionTitle: String? = "",
        val actionLink: String? = "",
        val appActionLink: String? = "",
        val titleList: List<String> = listOf(),
        val sellerSearchList: List<ItemSellerSearchUiModel> = listOf()
): BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }
}
