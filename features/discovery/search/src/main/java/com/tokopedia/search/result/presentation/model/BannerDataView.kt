package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class BannerDataView(
        val position: Int = -2,
        val text: String = "",
        val applink: String = "",
        val imageUrl: String = "",
): Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}