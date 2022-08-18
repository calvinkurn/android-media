package com.tokopedia.search.result.product.cpm

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class CpmDataView(
    val cpmModel: CpmModel = CpmModel(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
) : Visitable<ProductListTypeFactory>,
    VerticalSeparable {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun addTopSeparator(): VerticalSeparable = this

    override fun addBottomSeparator(): VerticalSeparable = this
}