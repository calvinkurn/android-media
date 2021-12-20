package com.tokopedia.product.addedit.variant.presentation.extension

import androidx.lifecycle.MutableLiveData
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel

fun MutableLiveData<ProductInputModel>.getValueOrDefault(
    defaultValue: ProductInputModel = ProductInputModel()
) = value ?: defaultValue

fun List<SelectionInputModel>.getSelectionLevelOptionTitle(level: Int, optionIndex: Int) =
    getOrNull(level)
        ?.options
        ?.getOrNull(optionIndex)
        ?.value.orEmpty()

