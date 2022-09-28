package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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
    val productVariant: LiveData<ReservedProduct.Product.ChildProduct> get() = _productVariant

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
            priceMessage = errorMessageHelper.getPriceMessage(criteria, discountSetup),
            pricePercentMessage = errorMessageHelper.getDiscountMessage(criteria, discountSetup)
        )
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

    fun setProduct(
        product: ReservedProduct.Product,
        positionOfVariant: Int,
        positionOfWarehouse: Int
    ) {
        val variant = product.childProducts[positionOfVariant]
        val reAssignMapWarehouse = reAssignMapWarehouse(variant.warehouses, positionOfWarehouse)
        product.childProducts[positionOfVariant].warehouses = reAssignMapWarehouse

        _product.value = product
        _productVariant.value = variant
    }

    fun reAssignMapWarehouse(
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

    fun servedByTokopedia() : ArrayList<Pair<Int, ReservedProduct.Product.Warehouse>>? {
        val listOfDilayaniTokopedia: ArrayList<Pair<Int, ReservedProduct.Product.Warehouse>> =
            arrayListOf()
        _productVariant.value?.warehouses.orEmpty().forEachIndexed { index, warehouse ->
            if (warehouse.isToggleOn && warehouse.isDilayaniTokopedia) {
                listOfDilayaniTokopedia.add(Pair(index, warehouse))
            }
        }

        return if (listOfDilayaniTokopedia.size >= MINIMUM_TO_SET_BULK)
            listOfDilayaniTokopedia
        else
            null
    }
}