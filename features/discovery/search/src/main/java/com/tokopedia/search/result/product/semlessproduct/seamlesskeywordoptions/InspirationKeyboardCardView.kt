package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class InspirationKeyboardCardView(
    val title: String = "",
    val optionsItems: List<InspirationKeywordDataView>,
) : Visitable<ProductListTypeFactory> {
    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    companion object {
        fun create(
            title: String,
            optionsItems: List<InspirationKeywordDataView>
        ): InspirationKeyboardCardView {
            return InspirationKeyboardCardView(
                title = title,
                optionsItems = optionsItems,
            )
        }
    }
}
