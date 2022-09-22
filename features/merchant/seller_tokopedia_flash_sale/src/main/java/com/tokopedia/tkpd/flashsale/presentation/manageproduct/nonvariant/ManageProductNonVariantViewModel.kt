package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import javax.inject.Inject

class ManageProductNonVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper
) : BaseViewModel(dispatchers.main){

    private val _isMultiloc: MutableLiveData<Boolean> = MutableLiveData()
    val isMultiloc: LiveData<Boolean> get() = _isMultiloc

    private val _enableBulkApply: MutableLiveData<Boolean> = MutableLiveData()
    val enableBulkApply: LiveData<Boolean> get() = _enableBulkApply

    private val _bulkApplyCaption: MutableLiveData<String> = MutableLiveData()
    val bulkApplyCaption: LiveData<String> get() = _bulkApplyCaption

    private val _isInputPageValid: MutableLiveData<Boolean> = MutableLiveData()
    val isInputPageValid: LiveData<Boolean> get() = _isInputPageValid

    fun validateInput(
        criteria: ProductCriteria,
        discountSetup: DiscountSetup
    ): ValidationResult {
        return ValidationResult(
            isPriceError = discountSetup.price !in criteria.minFinalPrice..criteria.maxFinalPrice,
            isPricePercentError = discountSetup.discount !in criteria.minDiscount..criteria.maxDiscount,
            isStockError = discountSetup.stock !in criteria.minCustomStock..criteria.maxCustomStock,
            priceMessage = errorMessageHelper.getPriceMessage(criteria, discountSetup),
            pricePercentMessage = errorMessageHelper.getDiscountMessage(criteria, discountSetup)
        )
    }

    fun checkMultiloc(product: ReservedProduct.Product) {
        _isMultiloc.value = product.isMultiWarehouse
    }

    fun checkEnableBulkApply(warehouses: List<Warehouse>) {
        _enableBulkApply.value = warehouses.any { it.isToggleOn }
    }

    fun getBulkApplyCaption(warehouses: List<Warehouse>) {
        _bulkApplyCaption.value = errorMessageHelper.getBulkApplyCaption(warehouses)
    }

    fun validateInputPage(warehouses: List<Warehouse>, criteria: ProductCriteria) {
        _isInputPageValid.value = warehouses
            .filter { it.isToggleOn }
            .all { validateInput(criteria, it.discountSetup).isAllFieldValid() }
    }

    fun calculatePrice(percentInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePrice(percentInput, originalPrice).toString()
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePercent(priceInput, originalPrice).toString()
    }
}