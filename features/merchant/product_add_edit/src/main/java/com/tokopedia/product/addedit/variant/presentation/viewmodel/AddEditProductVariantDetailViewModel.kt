package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_INACTIVE_STRING
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import kotlinx.coroutines.CoroutineDispatcher
import java.math.BigInteger
import javax.inject.Inject

class AddEditProductVariantDetailViewModel @Inject constructor(
        val provider: ResourceProvider, coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    var productInputModel = MutableLiveData<ProductInputModel>()

    val selectedVariantSize = MediatorLiveData<Int>().apply {
        addSource(productInputModel) {
            productInputModel.value?.run {
                value = this.variantInputModel.selections.size
            }
        }
    }

    val hasWholesale = Transformations.map(productInputModel) {
        it.detailInputModel.wholesaleList.isNotEmpty()
    }

    val isEditMode: Boolean get() = productInputModel.value?.productId.orZero() > 0

    private val mErrorCounter = MutableLiveData(0)
    val errorCounter: LiveData<Int> get() = mErrorCounter

    private var inputFieldSize = 0

    private var collapsedFields = 0

    private val headerStatusMap: HashMap<Int, Boolean> = hashMapOf()

    private val inputLayoutModelMap: HashMap<Int, VariantDetailInputLayoutModel> = hashMapOf()
    private val inputPriceErrorStatusMap: HashMap<Int, Boolean> = hashMapOf()
    private val inputStockErrorStatusMap: HashMap<Int, Boolean> = hashMapOf()

    fun getInputFieldSize(): Int {
        return inputFieldSize
    }

    fun setInputFieldSize(inputFieldSize: Int) {
        this.inputFieldSize = inputFieldSize
    }

    fun increaseCollapsedFields(inputFieldSize: Int) {
        collapsedFields += inputFieldSize
    }

    fun decreaseCollapsedFields(inputFieldSize: Int) {
        collapsedFields -= inputFieldSize
    }

    fun hasVariantCombination(selectedVariantSize: Int): Boolean {
        return selectedVariantSize == MAX_SELECTED_VARIANT_TYPE
    }

    fun isVariantDetailHeaderCollapsed(headerPosition: Int): Boolean {
        return if (headerStatusMap.containsKey(headerPosition)) {
            headerStatusMap[headerPosition] ?: false
        } else false
    }

    fun updateVariantDetailHeaderMap(adapterPosition: Int, isCollapsed: Boolean) {
        headerStatusMap[adapterPosition] = isCollapsed
    }

    fun updateVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        inputLayoutModelMap[fieldPosition] = variantDetailInputLayoutModel
    }

    fun editVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        if (inputLayoutModelMap.containsKey(fieldPosition)) inputLayoutModelMap[fieldPosition] = variantDetailInputLayoutModel
    }

    fun updateProductInputModel() {
        val products = productInputModel.value?.variantInputModel?.products.orEmpty()
        var index = 0
        inputLayoutModelMap.forEach {
            val variantDetail = it.value
            products.getOrNull(index)?.apply {
                price = variantDetail.price.replace(".", "").toBigIntegerOrNull().orZero()
                sku = variantDetail.sku
                stock = variantDetail.stock.toIntOrZero()
                status = if (variantDetail.isActive) STATUS_ACTIVE_STRING else STATUS_INACTIVE_STRING
                // the minimum product variant price will replace the main product price
                if (price < productInputModel.value?.detailInputModel?.price ?: 0.toBigInteger()) {
                    productInputModel.value?.detailInputModel?.price = price
                }
            }
            index++
        }
    }

    fun updateProductInputModel(inputModel: MultipleVariantEditInputModel) {
        productInputModel.value = productInputModel.value?.also {
            inputModel.selection.forEach { selectedCombination ->
                // search product variant by comparing combination
                val productVariant = it.variantInputModel.products
                        .find { it.combination == selectedCombination }
                // set value if found
                productVariant?.apply {
                    // assign new value if input price is not empty
                    if (inputModel.price.isNotEmpty()) {
                        price = inputModel.price.toBigIntegerOrNull().orZero()
                    }
                    // assign new value if input sku is not empty
                    if (inputModel.sku.isNotEmpty()) {
                        sku = inputModel.sku
                    }
                    stock = inputModel.stock.toInt()
                }
            }
        }
    }

    fun updatePrimaryVariant(combination: List<Int>) {
        val variantInputModel = productInputModel.value?.variantInputModel?.products
        variantInputModel?.forEach {
            it.isPrimary = it.combination == combination
        }
    }

    fun getPrimaryVariantTitle(combination: List<Int>): String {
        val selections = productInputModel.value?.variantInputModel?.selections ?: emptyList()
        val level1OptionIndex = combination.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION).orZero()
        val level2OptionIndex = combination.getOrNull(VARIANT_VALUE_LEVEL_TWO_POSITION).orZero()

        val level1Title = selections
                .getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION)?.options?.
                getOrNull(level1OptionIndex)?.value.orEmpty()

        val level2Title = selections
                .getOrNull(VARIANT_VALUE_LEVEL_TWO_POSITION)?.options?.
                getOrNull(level2OptionIndex)?.value.orEmpty()
        return "${level1Title}-${level2Title}"
    }

    fun updateSkuVisibilityStatus(isVisible: Boolean) {
        inputLayoutModelMap.forEach {
            it.value.isSkuFieldVisible = isVisible
        }
    }

    fun getVariantDetailHeaderData(headerPosition: Int): MutableList<VariantDetailInputLayoutModel> {
        val dataMap = inputLayoutModelMap.filterValues { it.headerPosition == headerPosition }
        return dataMap.values.toMutableList()
    }

    fun getAvailableFields(): Map<Int, VariantDetailInputLayoutModel> {
        // without combinations and headers
        if (headerStatusMap.isEmpty()) return inputLayoutModelMap
        // with variant combinations and headers
        val expandedHeaderPositions = mutableListOf<Int>()
        headerStatusMap.forEach {
            if (!it.value) expandedHeaderPositions.add(it.key)
        }
        val filteredMap = inputLayoutModelMap.filterValues { expandedHeaderPositions.contains(it.headerPosition) }
        if (collapsedFields != 0) {
            val fieldsMap = mutableMapOf<Int, VariantDetailInputLayoutModel>()
            filteredMap.forEach {
                val newFieldPosition = it.key - collapsedFields
                fieldsMap[newFieldPosition] = it.value
            }
            return fieldsMap
        }
        return filteredMap
    }

    fun updateSwitchStatus(isChecked: Boolean, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition]
                ?: VariantDetailInputLayoutModel()
        inputModel.isActive = isChecked
        return inputModel
    }

    fun updateVariantSkuInput(sku: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition]
                ?: VariantDetailInputLayoutModel()
        inputModel.sku = sku
        return inputModel
    }

    fun validateVariantPriceInput(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition]
                ?: VariantDetailInputLayoutModel()
        inputModel.price = priceInput
        if (priceInput.isEmpty()) {
            inputModel.isPriceError = true
            inputModel.priceFieldErrorMessage = provider.getEmptyProductPriceErrorMessage() ?: ""
            updateInputPriceErrorStatusMap(adapterPosition, true)

            return inputModel
        }
        val productPrice: BigInteger = priceInput.toBigIntegerOrNull().orZero()
        if (productPrice < MIN_PRODUCT_PRICE_LIMIT.toBigInteger()) {
            inputModel.isPriceError = true
            inputModel.priceFieldErrorMessage = provider.getMinLimitProductPriceErrorMessage() ?: ""
            updateInputPriceErrorStatusMap(adapterPosition, true)
            return inputModel
        }
        inputModel.isPriceError = false
        inputModel.priceFieldErrorMessage = ""
        updateInputPriceErrorStatusMap(adapterPosition, false)

        return inputModel
    }

    private fun updateInputPriceErrorStatusMap(adapterPosition: Int, isError: Boolean) {
        inputPriceErrorStatusMap[adapterPosition] = isError
        mErrorCounter.value = inputPriceErrorStatusMap.count { it.value }
    }

    fun validateProductVariantStockInput(stockInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition]
                ?: VariantDetailInputLayoutModel()
        inputModel.stock = stockInput
        if (stockInput.isEmpty()) {
            inputModel.isStockError = true
            inputModel.stockFieldErrorMessage = provider.getEmptyProductStockErrorMessage() ?: ""
            updateInputStockErrorStatusMap(adapterPosition, true)
            return inputModel
        }
        val productStock: BigInteger = stockInput.toBigIntegerOrNull().orZero()
        if (productStock < MIN_PRODUCT_STOCK_LIMIT.toBigInteger()) {
            inputModel.isStockError = true
            inputModel.stockFieldErrorMessage = provider.getMinLimitProductStockErrorMessage() ?: ""
            updateInputStockErrorStatusMap(adapterPosition, true)
            return inputModel
        }
        inputModel.isStockError = false
        inputModel.stockFieldErrorMessage = ""
        updateInputStockErrorStatusMap(adapterPosition, false)

        return inputModel
    }

    private fun updateInputStockErrorStatusMap(adapterPosition: Int, isError: Boolean) {
        inputStockErrorStatusMap[adapterPosition] = isError
        mErrorCounter.value = inputStockErrorStatusMap.count { it.value }
    }

    fun validateVariantPriceInput(priceInput: BigInteger): String {
        return when {
            priceInput < MIN_PRODUCT_PRICE_LIMIT.toBigInteger() -> {
                provider.getMinLimitProductPriceErrorMessage().orEmpty()
            }
            else -> ""
        }
    }

    fun validateProductVariantStockInput(stockInput: BigInteger): String {
        return when {
            stockInput < MIN_PRODUCT_STOCK_LIMIT.toBigInteger() -> {
                provider.getMinLimitProductStockErrorMessage().orEmpty()
            }
            else -> ""
        }
    }

}