package com.tokopedia.search.result.product.violation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

data class ViolationDataView(
    val headerText: String = "",
    val descriptionText: String = "",
    val imageUrl: String = "",
    val violationButton: ViolationButtonDataView = ViolationButtonDataView(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.Bottom,
) : Visitable<ProductListTypeFactory>, VerticalSeparable {

    companion object {
        fun create(
            violation: SearchProductModel.Violation,
        ): ViolationDataView? {
            if (!violation.isValid()) return null

            return ViolationDataView(
                violation.headerText,
                violation.descriptionText,
                violation.imageUrl,
                ViolationButtonDataView.create(violation),
                VerticalSeparator.Bottom,
            )
        }
    }

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun addTopSeparator(): VerticalSeparable = this

    override fun addBottomSeparator(): VerticalSeparable =
        this.copy(verticalSeparator = VerticalSeparator.Bottom)
}