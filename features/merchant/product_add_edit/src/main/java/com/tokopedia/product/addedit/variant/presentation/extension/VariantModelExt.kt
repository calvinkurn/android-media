package com.tokopedia.product.addedit.variant.presentation.extension

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel

fun MutableLiveData<ProductInputModel>.getValueOrDefault(
    defaultValue: ProductInputModel = ProductInputModel()
) = value ?: defaultValue

fun MutableLiveData<ProductInputModel>.hasVariant() =
    value?.variantInputModel?.products?.isNotEmpty().orFalse()

fun MutableLiveData<ProductInputModel>.removeVariantCombinations(removedPosition: Int, layoutPosition: Int) {
    val removedCombination = getValueOrDefault().variantInputModel.products.filterNot {
        it.combination[layoutPosition] == removedPosition
    }
    removedCombination.forEach {
        val combination = it.combination.toMutableList()
        if (combination[layoutPosition] > removedPosition) {
            combination[layoutPosition] = combination[layoutPosition].dec()
            it.combination = combination
        }
    }
    value?.variantInputModel?.products = removedCombination
}

fun List<SelectionInputModel>.getSelectionLevelOptionTitle(level: Int, optionIndex: Int) =
    getOrNull(level)
        ?.options
        ?.getOrNull(optionIndex)
        ?.value.orEmpty()