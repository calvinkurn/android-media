package com.tokopedia.product.addedit.variant.presentation.adapter

import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailFieldsUiModel
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailHeaderUiModel

interface VariantDetailInputTypeFactory {

    fun type(variantDetailHeaderUiModel: VariantDetailHeaderUiModel): Int

    fun type(variantDetailFieldsUiModel: VariantDetailFieldsUiModel): Int
}