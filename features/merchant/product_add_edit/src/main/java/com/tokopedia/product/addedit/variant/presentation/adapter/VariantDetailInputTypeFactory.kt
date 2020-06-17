package com.tokopedia.product.addedit.variant.presentation.adapter

import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailFieldsViewModel
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailHeaderViewModel

interface VariantDetailInputTypeFactory {

    fun type(variantDetailHeaderViewModel: VariantDetailHeaderViewModel): Int

    fun type(variantDetailFieldsViewModel: VariantDetailFieldsViewModel): Int
}