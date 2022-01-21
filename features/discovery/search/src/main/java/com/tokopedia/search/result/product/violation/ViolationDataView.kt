package com.tokopedia.search.result.product.violation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import kotlin.LazyThreadSafetyMode.NONE

data class ViolationDataView(
    val headerText: String = "",
    val descriptionText: String = "",
    val imageUrl: String = "",
    val ctaUrl: String = "",
    val buttonText: String = "",
    val buttonType: Int = BUTTON_DEFAULT_VALUE,
    val buttonVariant: Int = BUTTON_DEFAULT_VALUE
) : Visitable<ProductListTypeFactory> {

    companion object {
        const val BUTTON_DEFAULT_VALUE = -1
    }

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    val isButtonVisible: Boolean by lazy(NONE) {
        buttonText.isNotEmpty() && ctaUrl.isNotEmpty()
                && buttonType != BUTTON_DEFAULT_VALUE
                && buttonVariant != BUTTON_DEFAULT_VALUE
    }
}