package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class OrderSellerSearchUiModel(
        val id: String? = "",
        val title: String? = "",
        val desc: String? = "",
        val imageUrl: String? = "",
        val appUrl: String? = "",
        val url: String? = "",
        val keyword: String? = "",
        val section: String? = ""
): BaseInitialSearchSeller {
    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}