package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ManageProductNonVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper,
    private val getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase
) : BaseViewModel(dispatchers.main){

    private val _product: MutableLiveData<ReservedProduct.Product> = MutableLiveData()
    val product: LiveData<ReservedProduct.Product> get() = _product

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    private var criteria : CriteriaCheckingResult? = null

    val isMultiloc = Transformations.map(product) {
        it.isMultiWarehouse
    }

    val enableBulkApply = Transformations.map(product) {
        it.warehouses.any { warehouse -> warehouse.isToggleOn }
    }

    val bulkApplyCaption  = Transformations.map(product) {
        errorMessageHelper.getBulkApplyCaption(it.warehouses)
    }

    val isInputPageValid = Transformations.map(product) {
        val criteria = it.productCriteria
        it.warehouses
            .filter { warehouse -> warehouse.isToggleOn }
            .all { warehouse -> validateInput(criteria, warehouse.discountSetup).isAllFieldValid() }
    }

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

    fun calculatePrice(percentInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePrice(percentInput, originalPrice).toString()
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePercent(priceInput, originalPrice).toString()
    }

    fun setProduct(product: ReservedProduct.Product) {
        _product.value = product
    }

    fun checkCriteria(product : ReservedProduct.Product, campaignId : Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleProductCriteriaCheckingUseCase.execute(
                    productId = product.productId,
                    campaignId = campaignId,
                    productCriteriaId = product.productCriteria.criteriaId,
                )
                criteria = result.firstOrNull { it.name == product.name }
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun getCriteria() = criteria
}
