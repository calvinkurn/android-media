package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import javax.inject.Inject

//TODO WILLYBRODUS : Add all logic method in Adapter and Fragment into here
class ManageProductMultiLocationVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper
) : BaseViewModel(dispatchers.main) {

    private lateinit var productData: ReservedProduct.Product
    private lateinit var variant: ReservedProduct.Product.ChildProduct
    private var variantPositionOnProduct: Int = 0
    private var isHasEmptyState = arrayListOf<Int>()
    val buttonEnableState = MutableLiveData<Boolean>()

    fun setupInitiateProductData(product: ReservedProduct.Product, variantPositionOnProduct: Int) {
        productData = product
        variant = product.childProducts[variantPositionOnProduct]
        this.variantPositionOnProduct = variantPositionOnProduct
    }

    fun setItemToggleValue(itemPosition: Int, value: Boolean) {
        val selectedItem = variant.warehouses[itemPosition]
        selectedItem.isToggleOn = value
    }

    fun setDiscountAmount(itemPosition: Int, value: Long) {
        val selectedItem = variant.warehouses[itemPosition]
        selectedItem.price = value
    }

    fun setStockAmount(itemPosition: Int, value: Long) {
        val selectedItem = variant.warehouses[itemPosition]
        selectedItem.stock = value
    }

    fun setDiscountPercentage(itemPosition: Int, value: Long) {
        val selectedItem = variant.warehouses[itemPosition]
        selectedItem.discountSetup.discount = value.toInt()
    }

    fun getFinalProductData(): ReservedProduct.Product {
        return productData
    }

    fun checkAllValidationOfInputUser(position: Int, isAllInputEmpty: Boolean = false) {
        val isAllAmountValid = checkIsAllAmountInCriteria()
        val isAllStockValid = checkIsStockInCriteria()
        reViewIsHasEmptyState(position, isAllInputEmpty)
        buttonEnableState.value =
            if (isEmptyState()) false else (isAllAmountValid && isAllStockValid)
    }

    private fun reViewIsHasEmptyState(position: Int, isAllInputEmpty: Boolean) {
        if (!isAllInputEmpty)
            isHasEmptyState.remove(position)
        else {
            if (!isHasEmptyState.contains(position))
                isHasEmptyState.add(position)
        }
    }

    private fun isEmptyState() = isHasEmptyState.size > 0

    fun checkValidationDiscountAmountInWarehouseWithPosition(positionWarehouse: Int): Boolean {
        return checkIsAmountOnCriteria(variant.warehouses[positionWarehouse])
    }

    fun checkValidationDiscountPercentInWarehouseWithPosition(positionWarehouse: Int): Boolean {
        return checkIsPercentOnCriteria(variant.warehouses[positionWarehouse])
    }

    fun checkValidationStockInWarehouseWithPosition(positionWarehouse: Int): Boolean {
        return checkIsStockOnCriteria(variant.warehouses[positionWarehouse].stock)
    }

    fun getMessageOfHintInputField(positionWarehouse: Int): Pair<String, String> {
        return Pair(
            errorMessageHelper.getPriceMessage(
                productData.productCriteria,
                variant.warehouses[positionWarehouse].discountSetup
            ),
            errorMessageHelper.getDiscountMessage(
                productData.productCriteria,
                variant.warehouses[positionWarehouse].discountSetup
            ),
        )
    }

    fun getMessageOfHintStockField(positionWarehouse: Int): String {
        return errorMessageHelper.getStockMessage(
            productData.productCriteria,
            variant.warehouses[positionWarehouse].stock
        )

    }

    private fun checkIsAllAmountInCriteria(): Boolean {
        return variant.warehouses.any { warehouse ->
            if (warehouse.isToggleOn) {
                val resultCheck = checkIsAmountOnCriteria(warehouse)
                if (!resultCheck) {
                    return false
                }
                return@any resultCheck
            } else
                false
        }
    }

    private fun checkIsStockInCriteria(): Boolean {
        return variant.warehouses.any { warehouse ->
            if (warehouse.isToggleOn) {
                val resultCheck = checkIsStockOnCriteria(warehouse.stock)
                if (!resultCheck) {
                    return false
                }

                return@any resultCheck
            } else
                false
        }
    }

    private fun checkIsAmountOnCriteria(variant: ReservedProduct.Product.Warehouse): Boolean {
        val (productMinFinalPrice, productMaxFinalPrice) = getCriteriaPriceInProduct()
        return variant.price in productMinFinalPrice..productMaxFinalPrice
    }

    private fun checkIsPercentOnCriteria(variant: ReservedProduct.Product.Warehouse): Boolean {
        val (productMinPercent, productMaxPercent) = getCriteriaPercentInProduct()
        return variant.discountSetup.discount in productMinPercent..productMaxPercent
    }

    private fun checkIsStockOnCriteria(stock: Long): Boolean {
        val (productMinStock, productMaxStock) = getCriteriaStockInProduct()
        return stock in productMinStock..productMaxStock
    }

    private fun getCriteriaPriceInProduct(): Pair<Long, Long> {
        val productMinFinalPrice = productData.productCriteria.minFinalPrice
        val productMaxFinalPrice = productData.productCriteria.maxFinalPrice
        return Pair(productMinFinalPrice, productMaxFinalPrice)
    }

    private fun getCriteriaPercentInProduct(): Pair<Long, Long> {
        val productMinDiscount = productData.productCriteria.minDiscount
        val productMaxDiscount = productData.productCriteria.maxDiscount
        return Pair(productMinDiscount, productMaxDiscount)
    }

    private fun getCriteriaStockInProduct(): Pair<Int, Int> {
        val productMinStock = productData.productCriteria.minCustomStock
        val productMaxStock = productData.productCriteria.maxCustomStock
        return Pair(productMinStock, productMaxStock)
    }

    fun getVariantData(): ReservedProduct.Product.ChildProduct {
        return variant
    }

    fun getProductResult(): ReservedProduct.Product {
        return productData
    }
}