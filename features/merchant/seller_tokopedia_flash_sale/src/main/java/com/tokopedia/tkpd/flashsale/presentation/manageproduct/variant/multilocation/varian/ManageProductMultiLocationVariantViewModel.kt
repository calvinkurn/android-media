package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class ManageProductMultiLocationVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper,
    private val getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val MINIMUM_TO_SET_BULK = 2
        const val DEFAULT_SIZE_TO_BULK = 0
        const val DEBOUNCE_DELAY_MILLIS = 1000L
    }

    private var _product: MutableLiveData<ReservedProduct.Product> = MutableLiveData()
    val product: LiveData<ReservedProduct.Product>
        get() = _product

    private val _productVariant: MutableLiveData<ReservedProduct.Product.ChildProduct> =
        MutableLiveData()

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private var _variantPositionOnProduct: Int = 0
    private var criteria : CriteriaCheckingResult? = null

    val enableBulkApply = Transformations.map(_productVariant) {
        var sizeOfToggleOn = DEFAULT_SIZE_TO_BULK
        it.warehouses.forEach { warehouse -> if (warehouse.isToggleOn) sizeOfToggleOn++ }
        sizeOfToggleOn >= MINIMUM_TO_SET_BULK
    }

    val bulkApplyCaption = Transformations.map(_productVariant) {
        errorMessageHelper.getBulkApplyCaption(it.warehouses)
    }

    val isInputPageNotValid = Transformations.map(_product) {
        val criteria = it.productCriteria
        val listOfSelectedProductVariant = it.childProducts[_variantPositionOnProduct].warehouses
            .filter { warehouse -> warehouse.isToggleOn }

        val statusValidation = listOfSelectedProductVariant.any { warehouse ->
            return@any !validateInput(
                criteria,
                warehouse.discountSetup
            ).isAllFieldValid()
        }

        return@map statusValidation
    }


    //for tracking purpose
    private val _nominalDiscountInputTracker = MutableLiveData<String>()
    val doTrackingNominal = MutableLiveData<String>()

    private val _percentDiscountInputTracker = MutableLiveData<String>()
    val doTrackingPercent = MutableLiveData<String>()

    init{
        initNominalFlow()
        initPercentageFlow()
    }

    fun validateInput(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult {
        return ValidationResult(
            isPriceError = discountSetup.price !in criteria.minFinalPrice..criteria.maxFinalPrice,
            isPricePercentError = discountSetup.discount !in criteria.minDiscount..criteria.maxDiscount,
            isStockError = discountSetup.stock !in criteria.minCustomStock..criteria.maxCustomStock,
            priceMessage = getMessage(
                isPercent = false,
                isZero = false,
                criteria = criteria,
                discountSetup = discountSetup
            ),
            pricePercentMessage = getMessage(
                isPercent = true,
                isZero = false,
                criteria = criteria,
                discountSetup = discountSetup
            )
        )
    }

    fun validateItem(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult {
        val discountPrice = discountSetup.price.orZero()
        val discountPercent = discountSetup.discount.orZero()
        val stock = discountSetup.stock.orZero()
        val isDiscountPriceZero = discountPrice.isZero()
        val isDiscountPercentZero = discountPercent.isZero()
        val isStockZero = stock.isZero()
        val isDiscountPriceNotInCriteria =
            discountPrice !in criteria.minFinalPrice..criteria.maxFinalPrice
        val isDiscountPercentNotInCriteria =
            discountPercent !in criteria.minDiscount..criteria.maxDiscount
        val isStockNotInCriteria = stock !in criteria.minCustomStock..criteria.maxCustomStock
        val messageNominal = getMessage(false, isDiscountPriceZero, criteria, discountSetup)
        val messagePercent = getMessage(true, isDiscountPercentZero, criteria, discountSetup)
        return ValidationResult(
            isPriceError = isDiscountPriceNotInCriteria && !isDiscountPriceZero,
            isPricePercentError = isDiscountPercentNotInCriteria && !isDiscountPercentZero,
            isStockError = isStockNotInCriteria && !isStockZero,
            priceMessage = messageNominal,
            pricePercentMessage = messagePercent
        )
    }

    private fun getMessage(
        isPercent: Boolean,
        isZero: Boolean,
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): String {
        val messagePrice = errorMessageHelper.getString(
            R.string.manageproductnonvar_range_message_format,
            criteria.minFinalPrice.getCurrencyFormatted(),
            criteria.maxFinalPrice.getCurrencyFormatted()
        )
        val messagePercent = errorMessageHelper.getString(
            R.string.manageproductnonvar_range_message_format,
            criteria.minDiscount.getPercentFormatted(),
            criteria.maxDiscount.getPercentFormatted()
        )
        return when {
            isZero -> if (isPercent) messagePercent else messagePrice
            isPercent -> errorMessageHelper.getDiscountMessage(criteria, discountSetup)
            else -> errorMessageHelper.getPriceMessage(criteria, discountSetup)
        }
    }

    fun calculatePrice(percentInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePrice(percentInput, originalPrice).toString()
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePercent(priceInput, originalPrice).toString()
    }

    fun setProduct(product: ReservedProduct.Product, positionOfVariant: Int, flashSaleId: String) {
        _product.value = product
        _variantPositionOnProduct = positionOfVariant
        _productVariant.value = product.childProducts[positionOfVariant]
        checkCriteria(product, flashSaleId.toLong())
    }

    fun valueAdjustmentOfServedByTokopediaWarehouseToRegister(
        warehouses: List<ReservedProduct.Product.Warehouse>,
        positionWarehouse: Int
    ): List<ReservedProduct.Product.Warehouse> {
        val warehouse = warehouses[positionWarehouse]
        warehouses.forEach {
            if (it.isToggleOn && it.isDilayaniTokopedia && warehouse.isDilayaniTokopedia) {
                it.discountSetup.apply {
                    price = warehouse.discountSetup.price
                    discount = warehouse.discountSetup.discount
                }
            }
        }
        return warehouses
    }

    fun findPositionOfProductServedByTokopediaToRegister(listWarehouse: List<ReservedProduct.Product.Warehouse>): ArrayList<Pair<Int, ReservedProduct.Product.Warehouse>>? {
        val listOfServedByTokopedia: ArrayList<Pair<Int, ReservedProduct.Product.Warehouse>> =
            arrayListOf()
        listWarehouse.forEachIndexed { index, warehouse ->
            if (warehouse.isToggleOn && warehouse.isDilayaniTokopedia) {
                listOfServedByTokopedia.add(Pair(index, warehouse))
            }
        }

        return if (listOfServedByTokopedia.size >= MINIMUM_TO_SET_BULK)
            listOfServedByTokopedia
        else
            null
    }

    fun setToggleOnOrOf(
        product: ReservedProduct.Product?,
        positionOfVariant: Int
    ): ReservedProduct.Product? {
        return if (product != null) {
            val variant = product.childProducts[positionOfVariant]
            val isToggleOn = findToggleOnInWarehouse(variant).size.isMoreThanZero()
            variant.isToggleOn = isToggleOn
            product
        } else null
    }

    fun findToggleOnInWarehouse(variant: ReservedProduct.Product.ChildProduct): List<ReservedProduct.Product.Warehouse> {
        return variant.warehouses.filter { it.isToggleOn }
    }

    fun isValidCriteriaOnStock(
        stock: Long,
        minStockCriteria: Long
    ): Boolean {
        return minStockCriteria <= stock
    }

    fun isValidCriteriaOnPrice(
        price: Long,
        minPriceCriteria: Long,
        maxPriceCriteria: Long
    ): Boolean {
        return price in minPriceCriteria..maxPriceCriteria
    }

    fun isEligibleItem(
        warehouses: ReservedProduct.Product.Warehouse,
        criteria: ReservedProduct.Product.ProductCriteria,
    ): Boolean {
        val isValidCriteriaOnStock = isValidCriteriaOnStock(
            warehouses.stock,
            criteria.minCustomStock.toLong()
        )

        val isValidCriteriaOnPrice = isValidCriteriaOnPrice(
            warehouses.price,
            criteria.minFinalPrice,
            criteria.maxFinalPrice
        )

        return isValidCriteriaOnStock && isValidCriteriaOnPrice
    }

    private fun checkCriteria(product: ReservedProduct.Product, flashSaleId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleProductCriteriaCheckingUseCase.execute(
                    productId = product.productId,
                    campaignId = flashSaleId,
                    productCriteriaId = product.productCriteria.criteriaId,
                )
                criteria =
                    result.firstOrNull { it.name == product.childProducts[_variantPositionOnProduct].name }
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun doNominalDiscountTrackerInput(nominalInput: String) {
        _nominalDiscountInputTracker.value = nominalInput
    }

    private fun initNominalFlow() {
        viewModelScope.launch {
            _nominalDiscountInputTracker.asFlow()
                .debounce(DEBOUNCE_DELAY_MILLIS)
                .flowOn(dispatchers.io)
                .collect {
                    doTrackingNominal.value = it
                }
        }
    }

    fun doPercentageDiscountTrackerInput(percentInput: String) {
        _percentDiscountInputTracker.value = percentInput
    }

    private fun initPercentageFlow() {
        viewModelScope.launch {
            _percentDiscountInputTracker.asFlow()
                .debounce(DEBOUNCE_DELAY_MILLIS)
                .flowOn(dispatchers.io)
                .collect {
                    doTrackingPercent.postValue(it)
                }
        }
    }

    fun getCriteriaOn(selectedWarehouse: ReservedProduct.Product.Warehouse): CriteriaCheckingResult.LocationCheckingResult? {
        return criteria?.locationResult.orEmpty().firstOrNull { selectedWarehouse.name == it.cityName }
    }

}
