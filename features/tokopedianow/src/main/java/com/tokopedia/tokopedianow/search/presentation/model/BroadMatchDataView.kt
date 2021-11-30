package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory

data class BroadMatchDataView(
    val keyword: String = "",
    val applink: String = "",
    val broadMatchItemDataViewList: List<BroadMatchItemDataView> = listOf(),
    val dimension90: String = "",
): Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory?) =
        typeFactory?.type(this) ?: 0

}