package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class OrderSellerSearchUiModel(
        override val id: String? = "",
        override val title: String? = "",
        override val desc: String? = "",
        override val imageUrl: String? = "",
        override val appUrl: String? = "",
        override val url: String? = "",
        override val keyword: String? = "",
        override val section: String? = ""
): ItemSellerSearchUiModel(), BaseInitialSearchSeller {
    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}