package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEditProductVariantDetailViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    var productInputModel = MutableLiveData<ProductInputModel>()

    fun updateProductInputModel(inputModel: MultipleVariantEditInputModel) {
        // get combination from selected input
        inputModel.selection.forEachIndexed { index, values ->
            values.forEach { value ->
                // set new variant value to selected combination
                val combination = listOf(index, value.key)
                if (value.value) {
                    setProductVariantByCombination(combination, inputModel)
                }
            }
        }
    }

    private fun setProductVariantByCombination(combination: List<Int>, inputModel: MultipleVariantEditInputModel) {
        val variantInputModel = productInputModel.value?.variantInputModel?.products
        // search product variant by comparing combination
        val productVariant = variantInputModel?.findLast { it.combination == combination }
        // set value if found
        productVariant?.let {
            it.price = inputModel.price
            it.stock = inputModel.stock
            it.sku = inputModel.sku
        }
    }
}