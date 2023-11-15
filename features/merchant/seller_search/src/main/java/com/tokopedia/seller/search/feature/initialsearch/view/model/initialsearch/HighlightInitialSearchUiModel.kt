package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class HighlightInitialSearchUiModel(
    var highlightInitialList: List<ItemHighlightInitialSearchUiModel> = listOf()
) : BaseInitialSearchSeller {
    override fun getUniquePosition(): Int {
        return HIGHLIGHT_INITIAL_SEARCH_UI_MODEL.hashCode()
    }

    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val HIGHLIGHT_INITIAL_SEARCH_UI_MODEL = "HighlightInitialSearchUiModel"
    }
}
