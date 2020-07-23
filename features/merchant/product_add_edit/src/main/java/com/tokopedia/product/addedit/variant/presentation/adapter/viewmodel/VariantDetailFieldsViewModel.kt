package com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactory
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel

// TODO rename view model to ui model
class VariantDetailFieldsViewModel(val variantDetailInputLayoutModel: VariantDetailInputLayoutModel) : Visitable<VariantDetailInputTypeFactory> {
    override fun type(inputTypeFactory: VariantDetailInputTypeFactory): Int {
        return inputTypeFactory.type(this)
    }
}