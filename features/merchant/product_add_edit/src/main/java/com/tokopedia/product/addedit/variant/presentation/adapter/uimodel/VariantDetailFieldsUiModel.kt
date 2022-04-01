package com.tokopedia.product.addedit.variant.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactory
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel

class VariantDetailFieldsUiModel(
    val variantDetailInputLayoutModel: VariantDetailInputLayoutModel,
    val displayWeightCoachmark: Boolean = false
): Visitable<VariantDetailInputTypeFactory> {
    override fun type(inputTypeFactory: VariantDetailInputTypeFactory): Int {
        return inputTypeFactory.type(this)
    }
}