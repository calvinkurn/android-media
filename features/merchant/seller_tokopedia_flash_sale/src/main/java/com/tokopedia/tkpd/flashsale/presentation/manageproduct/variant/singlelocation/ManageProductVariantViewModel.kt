package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import javax.inject.Inject

class ManageProductVariantViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper
) : BaseViewModel(dispatchers.main) {

    private lateinit var productData: ReservedProduct.Product

    private val _isInputPageValid: MutableLiveData<Boolean> = MutableLiveData()
    val isInputPageValid: LiveData<Boolean>
        get() = _isInputPageValid

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

    fun setItemToggleValue(itemPosition: Int, value: Boolean) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.isToggleOn = value
        if (!selectedItem.isMultiwarehouse) {
            selectedItem.warehouses.map {
                it.isToggleOn = value
            }
        }
    }

    fun setDiscountValue(itemPosition: Int, priceValue: Long, discountValue: Int) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.warehouses.map { warehouse ->
            warehouse.discountSetup.price = priceValue
            warehouse.discountSetup.discount = discountValue
        }
    }

    fun setStockValue(itemPosition: Int, stockValue: Long) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.warehouses.map { warehouse ->
            warehouse.discountSetup.stock = stockValue
        }
    }

    fun validateInputPage(
        criteria: ReservedProduct.Product.ProductCriteria
    ) {
        if (productData.childProducts.any { it.isToggleOn }) {
            _isInputPageValid.value = productData.childProducts
                .filter { it.isToggleOn }
                .all { childProduct ->
                    if (childProduct.warehouses.any { it.isToggleOn }) {
                        childProduct.warehouses
                            .filter { warehouse -> warehouse.isToggleOn }
                            .all { warehouse ->
                                validateInput(
                                    criteria,
                                    warehouse.discountSetup
                                ).isAllFieldValid()
                            }
                    } else {
                        false
                    }
                }
        } else {
            _isInputPageValid.value = false
        }
    }

    fun calculatePrice(percentInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePrice(percentInput, originalPrice).toString()
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePercent(priceInput, originalPrice).toString()
    }
}