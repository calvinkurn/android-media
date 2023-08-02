package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class InspirationKeywordCardView(
    val title: String = "",
    val optionsItems: List<InspirationKeywordDataView>,
    val isOneOrMoreIsEmptyImage: Boolean = false
) : Visitable<ProductListTypeFactory> {
    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    companion object {
        fun create(
            title: String,
            optionsItems: List<InspirationKeywordDataView>,
            isOneOrMoreIsEmptyImage: Boolean
        ): InspirationKeywordCardView {
            return InspirationKeywordCardView(
                title = title,
                optionsItems = optionsItems,
                isOneOrMoreIsEmptyImage = isOneOrMoreIsEmptyImage
            )
        }
    }
}
