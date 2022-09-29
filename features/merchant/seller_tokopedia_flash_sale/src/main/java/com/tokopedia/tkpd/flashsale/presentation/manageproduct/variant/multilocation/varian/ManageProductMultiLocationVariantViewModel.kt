package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import javax.inject.Inject

class ManageProductMultiLocationVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val MINIMUM_TO_SET_BULK = 2
        const val DEFAULT_SIZE_TO_BULK = 0
    }

    private var _product: MutableLiveData<ReservedProduct.Product> = MutableLiveData()
    val product: LiveData<ReservedProduct.Product>
        get() = _product

    private val _productVariant: MutableLiveData<ReservedProduct.Product.ChildProduct> =
        MutableLiveData()

    val enableBulkApply = Transformations.map(_productVariant) {
        var sizeOfToggleOn = DEFAULT_SIZE_TO_BULK
        it.warehouses.forEach { warehouse -> if (warehouse.isToggleOn) sizeOfToggleOn++ }
        sizeOfToggleOn >= MINIMUM_TO_SET_BULK
    }

    val bulkApplyCaption = Transformations.map(_productVariant) {
        errorMessageHelper.getBulkApplyCaption(it.warehouses)
    }

    val isInputPageValid = Transformations.map(product) {
        val criteria = it.productCriteria
        val listOfSelectedProductVariant = _productVariant.value?.warehouses.orEmpty()
            .filter { warehouse -> warehouse.isToggleOn }

        listOfSelectedProductVariant.all { warehouse ->
            validateInput(
                criteria,
                warehouse.discountSetup
            ).isAllFieldValid()
        }
            .and(listOfSelectedProductVariant.size.isMoreThanZero())
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

    fun setProduct(product: ReservedProduct.Product, positionOfVariant: Int) {
        _product.value = product
        _productVariant.value = product.childProducts[positionOfVariant]
    }

    fun valueAdjustmentOfServedByTokopediaWarehouseToRegister(
        warehouses: List<ReservedProduct.Product.Warehouse>,
        positionWarehouse: Int
    ): List<ReservedProduct.Product.Warehouse> {
        val warehouse = warehouses[positionWarehouse]
        warehouses.forEach {
            if (it.isToggleOn && it.isDilayaniTokopedia) {
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
}