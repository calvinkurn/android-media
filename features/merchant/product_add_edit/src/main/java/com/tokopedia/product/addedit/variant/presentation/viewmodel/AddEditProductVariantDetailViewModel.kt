package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEditProductVariantDetailViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    var productInputModel = MutableLiveData<ProductInputModel>()

    val selectedVariantSize = MediatorLiveData<Int>().apply {
        addSource(productInputModel) {
            productInputModel.value?.run {
                value = this.variantInputModel.selections.size
            }
        }
    }

    private val variantDetailFieldMapLayout: HashMap<Int, VariantDetailInputLayoutModel> = hashMapOf()

    fun hasVariantCombination(selectedVariantSize: Int): Boolean {
        return selectedVariantSize == MAX_SELECTED_VARIANT_TYPE
    }

    fun updateVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        variantDetailFieldMapLayout[fieldPosition] = variantDetailInputLayoutModel
    }

    fun updateProductInputModel(inputModel: MultipleVariantEditInputModel) {
        val variantInputModel = productInputModel.value?.variantInputModel?.products
        inputModel.selection.forEach { selectedCombination ->
            // search product variant by comparing combination
            val productVariant = variantInputModel?.findLast { it.combination == selectedCombination }
            // set value if found
            productVariant?.let {
                it.price = inputModel.price
                it.stock = inputModel.stock
                it.sku = inputModel.sku
            }
        }
    }

    fun updatePrimaryVariant(combination: List<Int>) {
        val variantInputModel = productInputModel.value?.variantInputModel?.products
        variantInputModel?.forEach {
            it.isPrimary = it.combination == combination
        }
    }

    fun getVariantDetailFieldMap(): HashMap<Int, VariantDetailInputLayoutModel> {
        return this.variantDetailFieldMapLayout
    }

    fun showSkuFields(): HashMap<Int, VariantDetailInputLayoutModel> {
        variantDetailFieldMapLayout.forEach {
            it.value.isSkuFieldVisible = true
        }
        return variantDetailFieldMapLayout
    }

    fun hideSkuFields(): HashMap<Int, VariantDetailInputLayoutModel> {
        variantDetailFieldMapLayout.forEach {
            it.value.isSkuFieldVisible = false
        }
        return variantDetailFieldMapLayout
    }

    fun validatePriceField() {

    }

    fun validateStockField() {


    }

}