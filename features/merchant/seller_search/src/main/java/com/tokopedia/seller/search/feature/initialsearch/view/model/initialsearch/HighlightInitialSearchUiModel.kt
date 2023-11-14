package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class HighlightInitialSearchUiModel(
    var highlightInitialList: List<ItemHighlightInitialSearchUiModel> = listOf(),
    val position: Int
) : BaseInitialSearchSeller {
    override fun getUniquePosition(): Int {
        return position
    }

    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}
