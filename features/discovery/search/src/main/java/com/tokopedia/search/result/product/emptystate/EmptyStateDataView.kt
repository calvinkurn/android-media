package com.tokopedia.search.result.product.emptystate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

sealed class EmptyStateDataView: Visitable<ProductListTypeFactory?> {
    companion object {
        fun create(
            isFilterActive: Boolean,
            keyword: String,
            localSearch: LocalSearch?,
            isShowAdsLowOrganic: Boolean,
        ): EmptyStateDataView {
            return if (isFilterActive) EmptyStateFilterDataView
            else EmptyStateKeywordDataView(
                keyword = keyword,
                isLocalSearch = localSearch != null,
                globalSearchApplink = localSearch?.applink ?: "",
                pageTitle = localSearch?.pageTitle ?: "",
                isShowAdsLowOrganic = isShowAdsLowOrganic,
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
    val isShowAdsLowOrganic: Boolean = false,
): EmptyStateDataView(), VerticalSeparable {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override val verticalSeparator: VerticalSeparator =
        if (isShowAdsLowOrganic) VerticalSeparator.Bottom
        else VerticalSeparator.None

    override fun addTopSeparator(): VerticalSeparable = this

    override fun addBottomSeparator(): VerticalSeparable = this
}

object EmptyStateFilterDataView: EmptyStateDataView() {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
