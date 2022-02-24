package com.tokopedia.search.result.product.emptystate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

sealed class EmptyStateDataView: Visitable<ProductListTypeFactory?> {
    companion object {
        fun create(
            isFilterActive: Boolean,
            keyword: String,
            localSearch: LocalSearch?,
        ): EmptyStateDataView {
            return if (isFilterActive) EmptyStateFilterDataView
            else EmptyStateKeywordDataView(
                keyword = keyword,
                isLocalSearch = localSearch != null,
                globalSearchApplink = localSearch?.applink ?: "",
                pageTitle = localSearch?.pageTitle ?: "",
            )
        }
    }

    class LocalSearch(
        val applink: String = "",
        val pageTitle: String = "",
    )
}

class EmptyStateKeywordDataView(
    val keyword: String = "",
    val isLocalSearch: Boolean = false,
    val globalSearchApplink: String = "",
    val pageTitle: String = "",
): EmptyStateDataView() {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}

object EmptyStateFilterDataView: EmptyStateDataView() {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}