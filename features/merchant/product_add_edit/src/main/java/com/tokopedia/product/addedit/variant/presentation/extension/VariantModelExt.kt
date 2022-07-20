package com.tokopedia.product.addedit.variant.presentation.extension

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel

fun MutableLiveData<ProductInputModel>.getValueOrDefault(
    defaultValue: ProductInputModel = ProductInputModel()
) = value ?: defaultValue

fun HashMap<Int, Int>.firstHeaderPosition() = minByOrNull { it.key } ?: Int.ZERO

fun HashMap<Int, Int>.lastCurrentHeaderPosition() = maxByOrNull { it.value }?.value.orZero()

fun HashMap<Int, VariantDetailInputLayoutModel>.getValueOrDefault(
    position: Int,
    defaultValue: VariantDetailInputLayoutModel = VariantDetailInputLayoutModel()
) = this[position] ?: VariantDetailInputLayoutModel()

fun MutableLiveData<ProductInputModel>.hasVariant() =
    value?.variantInputModel?.products?.isNotEmpty().orFalse()

fun MutableLiveData<ProductInputModel>.removeVariantCombinations(removedPosition: Int, layoutPosition: Int) {
    val removedCombination = getValueOrDefault().variantInputModel.products.filterNot {
        it.combination.getOrNull(layoutPosition) == removedPosition
    }
    removedCombination.forEach {
        val combination = it.combination.toMutableList()
        if (combination.getOrNull(layoutPosition) == null) return@forEach
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