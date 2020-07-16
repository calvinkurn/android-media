package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class BroadMatchViewModel(
        val keyword: String = "",
        val url: String = "",
        val applink: String = "",
        val broadMatchItemViewModelList: List<BroadMatchItemViewModel> = listOf()
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}