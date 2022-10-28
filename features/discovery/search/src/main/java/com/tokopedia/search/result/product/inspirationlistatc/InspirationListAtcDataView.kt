package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

data class InspirationListAtcDataView(
    val option: Option = Option(),
    val type: String = "",
) : Visitable<ProductListTypeFactory>,
    VerticalSeparable {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    override val verticalSeparator: VerticalSeparator
        get() = VerticalSeparator.Both

    override fun addTopSeparator(): VerticalSeparable = this

    override fun addBottomSeparator(): VerticalSeparable = this

}
