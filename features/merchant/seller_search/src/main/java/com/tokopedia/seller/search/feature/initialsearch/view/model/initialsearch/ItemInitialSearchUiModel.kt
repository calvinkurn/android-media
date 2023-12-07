package com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

data class ItemInitialSearchUiModel(
    val id: String? = "",
    val title: String? = "",
    val desc: String? = "",
    val imageUrl: String? = "",
    val appUrl: String? = ""
) : BaseInitialSearchSeller {
    override fun getUniquePosition(): Int {
        return id.orEmpty().hashCode() + title.orEmpty().hashCode()
    }

    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }
}
