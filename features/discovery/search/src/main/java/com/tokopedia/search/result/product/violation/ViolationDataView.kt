package com.tokopedia.search.result.product.violation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class ViolationDataView(
    val headerText: String = "",
    val descriptionText: String = "",
    val imageUrl: String = "",
    val violationButton: ViolationButtonDataView = ViolationButtonDataView()
) : Visitable<ProductListTypeFactory> {

    companion object {
        fun create(
            headerText: String,
            descriptionText: String,
            imageUrl: String,
            ctaUrl: String,
            buttonText: String,
            buttonTypeVariant: String
        ) : ViolationDataView {
            return ViolationDataView(
                headerText,
                descriptionText,
                imageUrl,
                ViolationButtonDataView.create(ctaUrl, buttonText, buttonTypeVariant)
            )
        }
    }

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}