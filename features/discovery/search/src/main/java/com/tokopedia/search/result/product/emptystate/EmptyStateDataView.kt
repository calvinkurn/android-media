package com.tokopedia.search.result.product.emptystate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class EmptyStateDataView(
    val isFilterActive: Boolean = false,
    val keyword: String = "",
    val isLocalSearch: Boolean = false,
    val globalSearchApplink: String = "",
    val pageTitle: String = "",
) : Visitable<ProductListTypeFactory?> {

    class LocalSearch(
        val applink: String = "",
        val pageTitle: String = "",
    )

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    companion object {
        fun create(
            isFilterActive: Boolean,
            keyword: String,
            localSearch: LocalSearch?,
        ): EmptyStateDataView {
            return EmptyStateDataView(
                isFilterActive = isFilterActive,
                keyword = keyword,
                isLocalSearch = localSearch != null,
                globalSearchApplink = localSearch?.applink ?: "",
                pageTitle = localSearch?.pageTitle ?: "",
            )
        }
    }
}