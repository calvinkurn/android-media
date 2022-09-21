package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.tkpd.flashsale.util.constant.NumericalNormalizationConstant
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.round

class ManageProductVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper
) : BaseViewModel(dispatchers.main) {

    private lateinit var productData: ReservedProduct.Product

    private val _isInputPageValid: MutableLiveData<Boolean> = MutableLiveData()
    val isInputPageValid: LiveData<Boolean> get() = _isInputPageValid

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

    fun setupInitiateProductData(product: ReservedProduct.Product) {
        productData = product
    }

    fun getProductData(): ReservedProduct.Product {
        return productData
    }

    fun getModifiedProductData(): ReservedProduct.Product {
        val selectedChildProduct = productData.childProducts.filter {
            it.isToggleOn
        }
        productData.childProducts = selectedChildProduct
        return productData
    }

    fun setItemToggleValue(itemPosition: Int, value: Boolean) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.isToggleOn = value
    }

    fun setDiscountValue(itemPosition: Int, priceValue: Long, discountValue: Int) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.warehouses.first().discountSetup.price = priceValue
        selectedItem.warehouses.first().discountSetup.discount = discountValue
    }

    fun setStockValue(itemPosition: Int, stockValue: Long) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.warehouses.first().discountSetup.stock = stockValue
    }

    fun validateInputPage(
        warehouses: List<ReservedProduct.Product.Warehouse>,
        criteria: ReservedProduct.Product.ProductCriteria
    ) {
        if (productData.childProducts.any { it.isToggleOn }) {
            _isInputPageValid.value = productData.childProducts
                .filter { it.isToggleOn }
                .all { validateInput(criteria, warehouses.first().discountSetup).isAllFieldValid() }
        } else {
            _isInputPageValid.value = false
        }
    }

    fun calculatePrice(percentInput: Long, originalPrice: Long): String {
        return (percentInput * originalPrice / NumericalNormalizationConstant.BULK_APPLY_PERCENT_NORMALIZATION).toString()
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): String {
        return round((priceInput.toDouble() / originalPrice.toDouble()) * NumericalNormalizationConstant.BULK_APPLY_PERCENT_NORMALIZATION).toInt()
            .toString()
    }
}