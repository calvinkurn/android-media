package com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactory

class VariantDetailHeaderViewModel(val header: String, val position: Int) : Visitable<VariantDetailInputTypeFactory> {
    override fun type(inputTypeFactory: VariantDetailInputTypeFactory): Int {
        return inputTypeFactory.type(this)
    }
}