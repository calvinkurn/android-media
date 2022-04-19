package com.tokopedia.product.addedit.variant.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactory

class VariantDetailHeaderUiModel(val header: String, val position: Int) : Visitable<VariantDetailInputTypeFactory> {
    override fun type(inputTypeFactory: VariantDetailInputTypeFactory): Int {
        return inputTypeFactory.type(this)
    }
}