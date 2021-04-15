package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class BroadMatchDataView(
        val keyword: String = "",
        val url: String = "",
        val applink: String = "",
        val isAppendTitleInTokopedia: Boolean = false,
        val broadMatchItemDataViewList: List<BroadMatchItemDataView> = listOf(),
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}