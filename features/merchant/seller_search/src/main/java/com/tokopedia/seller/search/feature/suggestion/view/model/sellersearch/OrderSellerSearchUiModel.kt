package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactorySuggestionSearchAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller

data class OrderSellerSearchUiModel(
    override val id: String? = "",
    override val title: String? = "",
    override val desc: String? = "",
    override val appUrl: String? = "",
    override val url: String? = "",
    override val keyword: String? = "",
    override val section: String? = ""
) : ItemSellerSearchUiModel(), BaseSuggestionSearchSeller {
    override fun type(typeFactory: TypeFactorySuggestionSearchAdapter): Int {
        return typeFactory.type(this)
    }

    override fun getUniquePosition(): Int {
        return id.orEmpty().hashCode() + title.orEmpty().hashCode() + appUrl.orEmpty().hashCode()
    }
}
