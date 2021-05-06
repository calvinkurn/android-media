package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

class ItemTitleInitialSearchUiModel: BaseInitialSearchSeller {
    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}