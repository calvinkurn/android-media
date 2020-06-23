package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class InitialSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var count: Int? = 0,
        var titleList: List<String> = listOf(),
        var sellerSearchList: List<ItemInitialSearchUiModel> = listOf()
): BaseInitialSearchSeller {
    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}