package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine.LayoutType

data class InspirationKeywordCardView(
    val title: String = "",
    val optionsItems: List<InspirationKeywordDataView>,
    val isOneOrMoreIsEmptyImage: Boolean = false,
    val layoutType: LayoutType = LayoutType.DEFAULT_SEAMLESS,
    val searchTerm: String = "",
) : Visitable<ProductListTypeFactory> {
    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    companion object {
        fun create(
            title: String,
            optionsItems: List<InspirationKeywordDataView>,
            isOneOrMoreIsEmptyImage: Boolean,
            type: String,
            searchTerm: String
        ): InspirationKeywordCardView {
            return InspirationKeywordCardView(
                title = title,
                optionsItems = optionsItems,
                isOneOrMoreIsEmptyImage = isOneOrMoreIsEmptyImage,
                layoutType = LayoutType.getLayoutType(type),
                searchTerm = searchTerm
            )
        }
    }
}
