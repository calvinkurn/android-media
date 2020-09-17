package com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class TitleHeaderSellerSearchUiModel(val title: String): BaseInitialSearchSeller {
    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}