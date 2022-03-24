package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_INACTIVE_STRING
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.common.util.VariantResourceProvider
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.DEFAULT_IS_PRIMARY_INDEX
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_PRODUCT_WEIGHT_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_WEIGHT_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.extension.getSelectionLevelOptionTitle
import com.tokopedia.product.addedit.variant.presentation.extension.getValueOrDefault
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import java.math.BigInteger
import javax.inject.Inject

class AddEditProductVariantDetailViewModel @Inject constructor(
    val provider: ResourceProvider,
    val variantResourceProvider: VariantResourceProvider,
    private val userSession: UserSessionInterface,
    coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    var productInputModel = MutableLiveData<ProductInputModel>()

    private val mInputDataValid = MutableLiveData<Boolean>()
    val inputDataValid: LiveData<Boolean> get() = mInputDataValid

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

    val isEditMode: Boolean get() = productInputModel.getValueOrDefault().productId.orZero() > 0
    var isMultiLocationShop: Boolean = false
        private set

    private var inputFieldSize = 0
    private var collapsedFields = 0
    private val headerStatusMap: HashMap<Int, Boolean> = hashMapOf()
    private val currentHeaderPositionMap: HashMap<Int, Int> = hashMapOf()
    private val inputLayoutModelMap: HashMap<Int, VariantDetailInputLayoutModel> = hashMapOf()

    fun getInputFieldSize(): Int {
        return inputFieldSize
    }

    fun setInputFieldSize(inputFieldSize: Int) {
        this.inputFieldSize = inputFieldSize
    }

    fun getCollapsedFields(): Int {
        return collapsedFields
    }

    fun resetCollapsedFields() {
        this.collapsedFields = 0
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

    fun updateCurrentHeaderPositionMap(headerPosition: Int, visitablePosition: Int) {
        currentHeaderPositionMap[headerPosition] = visitablePosition
    }

    fun collapseHeader(collapsedHeaderPosition: Int, currentHeaderPosition: Int) {
        val firstHeaderPosition = currentHeaderPositionMap.minByOrNull { it.key } ?: 0
        val lastCurrentHeaderPosition = currentHeaderPositionMap.maxByOrNull { it.value }?.value ?: 0
        // collapsing last header position creates no impact
        if (currentHeaderPosition == lastCurrentHeaderPosition) return
        else {
            currentHeaderPositionMap.forEach {
                val headerPosition = it.key
                // first header position cannot be moved
                if (headerPosition != firstHeaderPosition) {
                    // only affecting the header position after the collapsing one
                    if (headerPosition > collapsedHeaderPosition) {
                        val newHeaderPosition = it.value - inputFieldSize
                        currentHeaderPositionMap[headerPosition] = newHeaderPosition
                    }
                }
            }
        }
    }

    fun expandHeader(expandedHeaderPosition: Int, currentHeaderPosition: Int) {
        val firstHeaderPosition = currentHeaderPositionMap.minByOrNull { it.key } ?: 0
        val lastCurrentHeaderPosition = currentHeaderPositionMap.maxByOrNull { it.value }?.value ?: 0
        // expanding last header position creates no impact
        if (currentHeaderPosition == lastCurrentHeaderPosition) return
        else {
            currentHeaderPositionMap.forEach {
                val headerPosition = it.key
                // first header position cannot be moved
                if (headerPosition != firstHeaderPosition) {
                    // only affecting the header position after the expanding one
                    if (headerPosition > expandedHeaderPosition) {
                        val newHeaderPosition = it.value + inputFieldSize
                        currentHeaderPositionMap[headerPosition] = newHeaderPosition
                    }
                }
            }
        }
    }

    fun addToVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        inputLayoutModelMap[fieldPosition] = variantDetailInputLayoutModel
    }

    fun updateVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        if (inputLayoutModelMap.containsKey(fieldPosition)) {
            inputLayoutModelMap[fieldPosition] = variantDetailInputLayoutModel
            refreshInputDataValidStatus()
        }
    }

    private fun refreshInputDataValidStatus() {
        mInputDataValid.value = !inputLayoutModelMap.any {
            it.value.isPriceError || it.value.isStockError || it.value.isWeightError
        }
    }

    fun updateProductInputModel(productInputModel: ProductInputModel) {
        this.productInputModel.value = productInputModel
        setDefaultPrimaryVariant() // set default variant if don't have any
    }

    private fun setDefaultPrimaryVariant() {
        productInputModel.getValueOrDefault().variantInputModel.products.let { products ->
            val isPrimaryVariantExist = products.any {
                it.isPrimary
            }

            if (!isPrimaryVariantExist) {
                products.getOrNull(DEFAULT_IS_PRIMARY_INDEX)?.isPrimary = true
            }
        }
    }

    fun updateProductInputModel() {
        val products = productInputModel.getValueOrDefault().variantInputModel.products
        var productPosition = 0
        inputLayoutModelMap.toSortedMap().forEach {
            val variantDetailInput = it.value
            products.getOrNull(productPosition)?.apply {
                price = variantDetailInput.price.replace(".", "").toBigIntegerOrNull().orZero()
                sku = variantDetailInput.sku
                stock = variantDetailInput.stock.toIntOrZero()
                weight = variantDetailInput.weight
                status = if (variantDetailInput.isActive) STATUS_ACTIVE_STRING else STATUS_INACTIVE_STRING
                // the minimum product variant price will replace the main product price
                if (price < productInputModel.value?.detailInputModel?.price ?: 0.toBigInteger()) {
                    productInputModel.value?.detailInputModel?.price = price
                }
                combination = variantDetailInput.combination
            }
            productPosition++
        }

        updateProductStatus(inputLayoutModelMap)
    }

    private fun updateProductStatus(inputLayoutModelMap: HashMap<Int, VariantDetailInputLayoutModel>) {
        val isAllProductDeactivated = inputLayoutModelMap.all { !it.value.isActive }
        productInputModel.getValueOrDefault().detailInputModel.status = if (isAllProductDeactivated) {
            ProductStatus.STATUS_INACTIVE
        } else {
            ProductStatus.STATUS_ACTIVE
        }
    }

    fun updateProductInputModel(inputModel: MultipleVariantEditInputModel) {
        val variantDetailInputMap = mutableMapOf<List<Int>, Boolean>()
        inputLayoutModelMap.forEach {
            variantDetailInputMap[it.value.combination] = it.value.isActive
        }

        productInputModel.value = productInputModel.getValueOrDefault().also {
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
                    // assign new value if input stock is not empty
                    if (inputModel.stock.isNotEmpty()) {
                        stock = inputModel.stock.toIntOrZero()
                    }
                    // assign new value if input sku is not empty
                    if (inputModel.sku.isNotEmpty()) {
                        sku = inputModel.sku
                    }
                    // assign new value if input weight is not empty
                    if (inputModel.weight.isNotEmpty()) {
                        weight = inputModel.weight.toIntOrZero()
                    }

                    status = if(variantDetailInputMap[selectedCombination] == true) STATUS_ACTIVE_STRING else STATUS_INACTIVE_STRING
                }
            }
        }
    }

    fun updatePrimaryVariant(combination: List<Int>): Int {
        var updatedPosition = -1 // -1 for not found
        val variantInputModels = productInputModel.getValueOrDefault().variantInputModel.products
        var variantInputModelsIndex = 0

        inputLayoutModelMap.toSortedMap().forEach { variantDetailInputModel ->
            variantInputModels.getOrNull(variantInputModelsIndex)?.let {
                if (it.combination == combination) {
                    it.isPrimary = true
                    it.status = STATUS_ACTIVE_STRING
                    updatedPosition = variantDetailInputModel.key
                } else {
                    it.isPrimary = false
                }
            }
            variantInputModelsIndex++
        }

        return updatedPosition
    }

    fun getPrimaryVariantTitle(combination: List<Int>): String {
        val selections = productInputModel.getValueOrDefault().variantInputModel.selections
        val level1OptionIndex = combination.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION).orZero()
        val level2OptionIndex = combination.getOrNull(VARIANT_VALUE_LEVEL_TWO_POSITION).orZero()
        val level1Title = selections.getSelectionLevelOptionTitle(VARIANT_VALUE_LEVEL_ONE_POSITION, level1OptionIndex)
        val level2Title = selections.getSelectionLevelOptionTitle(VARIANT_VALUE_LEVEL_TWO_POSITION, level2OptionIndex)

        return "${level1Title}-${level2Title}"
    }

    fun updateSkuVisibilityStatus(isVisible: Boolean) {
        inputLayoutModelMap.forEach {
            it.value.isSkuFieldVisible = isVisible
        }
        refreshCollapsedVariantDetailField()
    }

    fun getCurrentHeaderPosition(headerPosition: Int): Int {
        return currentHeaderPositionMap[headerPosition].orZero()
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
        if (collapsedFields > 0) {
            val fieldsMap = mutableMapOf<Int, VariantDetailInputLayoutModel>()
            filteredMap.forEach {
                val newFieldPosition = if (it.key <= collapsedFields) {
                    it.key
                } else {
                    it.key - collapsedFields
                }
                fieldsMap[newFieldPosition] = it.value
            }
            return fieldsMap
        }
        return filteredMap
    }

    fun updateSwitchStatus(isChecked: Boolean, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition] ?: VariantDetailInputLayoutModel()
        inputModel.isActive = isChecked
        return inputModel
    }

    fun updateVariantSkuInput(sku: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition] ?: VariantDetailInputLayoutModel()
        inputModel.sku = sku
        return inputModel
    }

    fun validateVariantPriceInput(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition] ?: VariantDetailInputLayoutModel()
        inputModel.price = priceInput
        if (priceInput.isEmpty()) {
            inputModel.priceFieldErrorMessage = variantResourceProvider.getEmptyProductPriceErrorMessage()
        } else {
            val productPrice = priceInput.toBigIntegerOrNull().orZero()
            inputModel.priceFieldErrorMessage = validateVariantPriceInput(productPrice)
        }
        inputModel.isPriceError = inputModel.priceFieldErrorMessage.isNotEmpty()
        return inputModel
    }

    fun validateProductVariantStockInput(stockInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition] ?: VariantDetailInputLayoutModel()
        inputModel.stock = stockInput
        if (stockInput.isEmpty()) {
            inputModel.stockFieldErrorMessage = variantResourceProvider.getEmptyProductStockErrorMessage()
        } else {
            val productStock = stockInput.toBigIntegerOrNull().orZero()
            inputModel.stockFieldErrorMessage = validateProductVariantStockInput(productStock)
        }
        inputModel.isStockError = inputModel.stockFieldErrorMessage.isNotEmpty()
        return inputModel
    }

    fun validateProductVariantWeightInput(weightInput: Int?, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap[adapterPosition] ?: VariantDetailInputLayoutModel()
        if (weightInput == null) {
            inputModel.weightFieldErrorMessage = variantResourceProvider.getEmptyProductWeightErrorMessage()
            inputModel.weight = Int.ZERO
        } else {
            inputModel.weightFieldErrorMessage = validateProductVariantWeightInput(weightInput)
            inputModel.weight = weightInput
        }
        inputModel.isWeightError = inputModel.weightFieldErrorMessage.isNotEmpty()
        return inputModel
    }

    fun validateVariantPriceInput(priceInput: BigInteger): String {
        return when {
            priceInput < MIN_PRODUCT_PRICE_LIMIT.toBigInteger() -> {
                variantResourceProvider.getMinLimitProductPriceErrorMessage(MIN_PRODUCT_PRICE_LIMIT)
            }
            else -> ""
        }
    }

    fun validateProductVariantStockInput(stockInput: BigInteger): String {
        return when {
            stockInput < MIN_PRODUCT_STOCK_LIMIT.toBigInteger() -> {
                variantResourceProvider.getMinLimitProductStockErrorMessage(MIN_PRODUCT_STOCK_LIMIT)
            }
            stockInput > MAX_PRODUCT_STOCK_LIMIT.toBigInteger() -> {
                variantResourceProvider.getMaxLimitProductStockErrorMessage(MAX_PRODUCT_STOCK_LIMIT)
            }
            else -> ""
        }
    }

    fun validateProductVariantWeightInput(weightInput: Int): String {
        return when {
            weightInput < MIN_PRODUCT_WEIGHT_LIMIT -> {
                variantResourceProvider.getMinLimitProductWeightErrorMessage(MIN_PRODUCT_WEIGHT_LIMIT)
            }
            weightInput > MAX_PRODUCT_WEIGHT_LIMIT -> {
                variantResourceProvider.getMaxLimitProductWeightErrorMessage(MAX_PRODUCT_WEIGHT_LIMIT)
            }
            else -> ""
        }
    }

    fun validateSubmitDetailField(
            variantDetailInputLayoutModels: List<VariantDetailInputLayoutModel>
    ): Boolean {
        return variantDetailInputLayoutModels.any {
            val productPrice: BigInteger = it.price.replace(".", "").toBigIntegerOrNull().orZero()
            val productStock: BigInteger = it.stock.replace(".", "").toBigIntegerOrNull().orZero()
            productPrice < MIN_PRODUCT_PRICE_LIMIT.toBigInteger() ||
                    productStock < MIN_PRODUCT_STOCK_LIMIT.toBigInteger()
        }
    }

    fun generateVariantDetailInputModel(
            productVariantIndex: Int,
            headerPosition: Int,
            unitValueLabel: String,
            isSkuFieldVisible: Boolean
    ): VariantDetailInputLayoutModel{
        val productVariants = productInputModel.getValueOrDefault().variantInputModel.products
        val productVariant = productVariants
                .getOrElse(productVariantIndex) { ProductVariantInputModel() }
        val priceString = productVariant.price.toString()
        val isPrimary = productVariant.isPrimary
        val combination = productVariant.combination

        return VariantDetailInputLayoutModel(
                price = InputPriceUtil.formatProductPriceInput(priceString),
                isActive = productVariant.status == STATUS_ACTIVE_STRING,
                sku = productVariant.sku,
                stock = productVariant.stock.toString(),
                weight = productVariant.weight,
                headerPosition = headerPosition,
                isSkuFieldVisible = isSkuFieldVisible,
                unitValueLabel = unitValueLabel,
                isPrimary = isPrimary,
                combination = combination)
    }

    fun setupMultiLocationValue() {
        isMultiLocationShop = userSession.run {
            isMultiLocationShop && (isShopAdmin || isShopOwner)
        }
    }

    private fun refreshCollapsedVariantDetailField() {
        updateProductInputModel() // save the last input
        // reset collapsed variables
        collapsedFields = 0
        headerStatusMap.forEach {
            headerStatusMap[it.key] = false
        }
        // do refresh
        productInputModel.value = productInputModel.value // force invoke livedata notify observer
    }

}