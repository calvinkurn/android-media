package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import com.tokopedia.seller.search.common.presentation.model.ItemTitleHighlightUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class ItemTitleHighlightInitialSearchUiModel(
        override val title: String = ""
): ItemTitleHighlightUiModel(), BaseInitialSearchSeller {
    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}