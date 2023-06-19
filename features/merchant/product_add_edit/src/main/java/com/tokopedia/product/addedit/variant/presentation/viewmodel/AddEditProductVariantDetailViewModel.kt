package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_INACTIVE_STRING
import com.tokopedia.product.addedit.common.util.IMSResourceProvider
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.DEFAULT_IS_PRIMARY_INDEX
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_PRODUCT_WEIGHT_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_WEIGHT_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.extension.firstHeaderPosition
import com.tokopedia.product.addedit.variant.presentation.extension.getSelectionLevelOptionTitle
import com.tokopedia.product.addedit.variant.presentation.extension.getValueOrDefault
import com.tokopedia.product.addedit.variant.presentation.extension.lastCurrentHeaderPosition
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.getVariantName
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection
import com.tokopedia.shop.common.domain.interactor.GetMaxStockThresholdUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import java.math.BigInteger
import javax.inject.Inject

class AddEditProductVariantDetailViewModel @Inject constructor(
    private val imsResourceProvider: IMSResourceProvider,
    private val getMaxStockThresholdUseCase: GetMaxStockThresholdUseCase,
    private val userSession: UserSessionInterface,
    coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher) {

    var productInputModel = MutableLiveData<ProductInputModel>()

    private val _maxStockThreshold = MutableLiveData<String>()
    val maxStockThreshold: LiveData<String>
        get() = _maxStockThreshold

    var isMultiLocationShop: Boolean = false
        private set

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

    private var inputFieldSize = 0
    private val headerStatusMap: HashMap<Int, Boolean> = hashMapOf()
    private val currentHeaderPositionMap: HashMap<Int, Int> = hashMapOf()
    private val inputLayoutModelMap: HashMap<Int, VariantDetailInputLayoutModel> = hashMapOf()

    private fun refreshCollapsedVariantDetailField() {
        updateProductInputModel() // save the last input
        headerStatusMap.forEach {
            headerStatusMap[it.key] = false
        }
        // do refresh
        productInputModel.value = productInputModel.value // force invoke livedata notify observer
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

    private fun updateProductStatus(inputLayoutModelMap: HashMap<Int, VariantDetailInputLayoutModel>) {
        val isAllProductDeactivated = inputLayoutModelMap.all { !it.value.isActive }
        productInputModel.getValueOrDefault().detailInputModel.status = if (isAllProductDeactivated) {
            ProductStatus.STATUS_INACTIVE
        } else {
            ProductStatus.STATUS_ACTIVE
        }
    }

    private fun updateProductParentStock() {
        productInputModel.getValueOrDefault().let {
            it.shipmentInputModel.weight = it.variantInputModel.getPrimaryVariantData().weight.orZero()
        }
    }

    private fun modifyHeaderPosition(firstHeaderPosition: Any,
                                     modifiedHeaderPosition: Int,
                                     inputFieldSize: Int) {
        currentHeaderPositionMap.forEach {
            val headerPosition = it.key
            // first header position cannot be moved
            if (headerPosition != firstHeaderPosition) {
                // only affecting the header position after the collapsing one
                if (headerPosition > modifiedHeaderPosition) {
                    val newHeaderPosition = it.value + inputFieldSize
                    currentHeaderPositionMap[headerPosition] = newHeaderPosition
                }
            }
        }
    }

    fun getInputFieldSize(): Int {
        return inputFieldSize
    }

    fun setInputFieldSize(inputFieldSize: Int) {
        this.inputFieldSize = inputFieldSize
    }

    fun hasVariantCombination(selectedVariantSize: Int): Boolean {
        return selectedVariantSize == MAX_SELECTED_VARIANT_TYPE
    }

    fun isVariantDetailHeaderCollapsed(headerPosition: Int): Boolean {
        return if (headerStatusMap.containsKey(headerPosition)) {
            headerStatusMap[headerPosition].orFalse()
        } else false
    }

    fun updateVariantDetailHeaderMap(adapterPosition: Int, isCollapsed: Boolean) {
        headerStatusMap[adapterPosition] = isCollapsed
    }

    fun updateCurrentHeaderPositionMap(headerPosition: Int, visitablePosition: Int) {
        currentHeaderPositionMap[headerPosition] = visitablePosition
    }

    fun collapseHeader(collapsedHeaderPosition: Int, currentHeaderPosition: Int) {
        val firstHeaderPosition = currentHeaderPositionMap.firstHeaderPosition()
        val lastCurrentHeaderPosition = currentHeaderPositionMap.lastCurrentHeaderPosition()
        // collapsing last header position creates no impact
        if (currentHeaderPosition == lastCurrentHeaderPosition) return
        else {
            val fieldSize = -inputFieldSize
            modifyHeaderPosition(firstHeaderPosition, collapsedHeaderPosition, fieldSize)
        }
    }

    fun expandHeader(expandedHeaderPosition: Int, currentHeaderPosition: Int) {
        val firstHeaderPosition = currentHeaderPositionMap.firstHeaderPosition()
        val lastCurrentHeaderPosition = currentHeaderPositionMap.lastCurrentHeaderPosition()
        // expanding last header position creates no impact
        if (currentHeaderPosition == lastCurrentHeaderPosition) return
        else {
            modifyHeaderPosition(firstHeaderPosition, expandedHeaderPosition, inputFieldSize)
        }
    }

    fun addToVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        inputLayoutModelMap[fieldPosition] = variantDetailInputLayoutModel
    }

    fun updateVariantDetailInputMap(fieldPosition: Int, variantDetailInputLayoutModel: VariantDetailInputLayoutModel) {
        if (inputLayoutModelMap.containsKey(fieldPosition)) {
            inputLayoutModelMap[fieldPosition] = variantDetailInputLayoutModel
        }
    }

    fun updateProductInputModel(productInputModel: ProductInputModel) {
        this.productInputModel.value = productInputModel
        setDefaultPrimaryVariant() // set default variant if don't have any
    }

    fun updateProductInputModel() {
        val products = productInputModel.getValueOrDefault().variantInputModel.products
        var productPosition = Int.ZERO
        inputLayoutModelMap.toSortedMap().forEach {
            val variantDetailInput = it.value
            products.getOrNull(productPosition)?.apply {
                price = variantDetailInput.price.replace(".", "").toBigIntegerOrNull().orZero()
                sku = variantDetailInput.sku
                stock = variantDetailInput.stock
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
        updateProductParentStock()
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

    fun getMaxStockThreshold(shopId: String) {
        launchCatchError(block = {
            _maxStockThreshold.value = getMaxStockThresholdUseCase.execute(shopId).getIMSMeta.data.maxStockThreshold
        }, onError = {
            _maxStockThreshold.value = null
        })
    }

    fun getCurrentHeaderPosition(headerPosition: Int): Int {
        return currentHeaderPositionMap[headerPosition].orZero()
    }

    fun getVariantDetailHeaderData(headerPosition: Int): MutableList<VariantDetailInputLayoutModel> {
        val dataMap = inputLayoutModelMap.filterValues { it.headerPosition == headerPosition }
        return dataMap.values.toMutableList()
    }

    fun updateSwitchStatus(isChecked: Boolean, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap.getValueOrDefault(adapterPosition)
        inputModel.isActive = isChecked
        return inputModel
    }

    fun updateVariantSkuInput(sku: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap.getValueOrDefault(adapterPosition)
        inputModel.sku = sku
        return inputModel
    }

    fun validateVariantPriceInput(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap.getValueOrDefault(adapterPosition)
        inputModel.price = priceInput
        if (priceInput.isEmpty()) {
            inputModel.priceFieldErrorMessage = imsResourceProvider.getEmptyProductPriceErrorMessage()
        } else {
            val productPrice = priceInput.toBigIntegerOrNull().orZero()
            inputModel.priceFieldErrorMessage = validateVariantPriceInput(productPrice)
        }
        inputModel.isPriceError = inputModel.priceFieldErrorMessage.isNotEmpty()
        return inputModel
    }

    fun validateProductVariantStockInput(stockInput: Int?, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap.getValueOrDefault(adapterPosition)
        inputModel.stock = stockInput
        if (stockInput == null) {
            inputModel.stockFieldErrorMessage = imsResourceProvider.getEmptyProductStockErrorMessage()
        } else {
            val productStock = stockInput.toBigInteger()
            inputModel.stockFieldErrorMessage = validateProductVariantStockInput(productStock)
        }
        inputModel.isStockError = inputModel.stockFieldErrorMessage.isNotEmpty()
        return inputModel
    }

    fun validateProductVariantWeightInput(weightInput: Int?, adapterPosition: Int): VariantDetailInputLayoutModel {
        val inputModel = inputLayoutModelMap.getValueOrDefault(adapterPosition)
        inputModel.weight = weightInput
        if (weightInput == null) {
            inputModel.weightFieldErrorMessage = imsResourceProvider.getEmptyProductWeightErrorMessage()
        } else {
            inputModel.weightFieldErrorMessage = validateProductVariantWeightInput(weightInput)
        }
        inputModel.isWeightError = inputModel.weightFieldErrorMessage.isNotEmpty()
        return inputModel
    }

    fun validateVariantPriceInput(priceInput: BigInteger): String {
        return when {
            priceInput < MIN_PRODUCT_PRICE_LIMIT.toBigInteger() -> {
                imsResourceProvider.getMinLimitProductPriceErrorMessage(MIN_PRODUCT_PRICE_LIMIT)
            }
            else -> ""
        }
    }

    fun validateProductVariantStockInput(stockInput: BigInteger): String {
        val maxStock = _maxStockThreshold.value
        val isMaxStockNotNull = maxStock != null
        val isCurrentStockLessThanMinStock = stockInput < MIN_PRODUCT_STOCK_LIMIT.toBigInteger()
        val isCurrentStockMoreThanMaxStock = stockInput > maxStock?.toBigIntegerOrNull().orZero()
        return when {
            isCurrentStockLessThanMinStock -> {
                imsResourceProvider.getMinLimitProductStockErrorMessage(MIN_PRODUCT_STOCK_LIMIT)
            }
            isMaxStockNotNull && isCurrentStockMoreThanMaxStock -> {
                imsResourceProvider.getMaxLimitProductStockErrorMessage(maxStock)
            }
            else -> ""
        }
    }

    fun validateProductVariantWeightInput(weightInput: Int): String {
        return when {
            weightInput < MIN_PRODUCT_WEIGHT_LIMIT -> {
                imsResourceProvider.getMinLimitProductWeightErrorMessage(MIN_PRODUCT_WEIGHT_LIMIT)
            }
            weightInput > MAX_PRODUCT_WEIGHT_LIMIT -> {
                imsResourceProvider.getMaxLimitProductWeightErrorMessage(MAX_PRODUCT_WEIGHT_LIMIT)
            }
            else -> ""
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
                stock = productVariant.stock,
                weight = productVariant.weight,
                headerPosition = headerPosition,
                isSkuFieldVisible = isSkuFieldVisible,
                unitValueLabel = unitValueLabel,
                isPrimary = isPrimary,
                combination = combination,
                hasDTStock = productVariant.hasDTStock,
                isCampaignActive = productVariant.isCampaign)
    }

    fun getInputDataValidStatus() = !inputLayoutModelMap.any {
        it.value.isPriceError || it.value.isStockError || it.value.isWeightError
    }

    fun setupMultiLocationValue() {
        isMultiLocationShop = userSession.run {
            isMultiLocationShop && (isShopAdmin || isShopOwner)
        }
    }
}
