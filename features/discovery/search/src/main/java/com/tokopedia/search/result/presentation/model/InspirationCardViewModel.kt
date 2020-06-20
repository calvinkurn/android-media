package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class InspirationCardViewModel(
        val title: String = "",
        val type: String = "",
        val position: Int = 0,
        val options: List<InspirationCardOptionViewModel> = listOf()
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}