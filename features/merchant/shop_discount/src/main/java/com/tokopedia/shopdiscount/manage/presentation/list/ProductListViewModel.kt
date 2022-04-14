package com.tokopedia.shopdiscount.manage.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.manage.data.mapper.ProductMapper
import com.tokopedia.shopdiscount.manage.data.mapper.UpdateDiscountRequestMapper
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.domain.usecase.DeleteDiscountUseCase
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListUseCase
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListUseCase: GetSlashPriceProductListUseCase,
    private val deleteDiscountUseCase: DeleteDiscountUseCase,
    private val reserveProductUseCase : MutationDoSlashPriceProductReservationUseCase,
    private val productMapper: ProductMapper,
    private val updateDiscountRequestMapper: UpdateDiscountRequestMapper
) : BaseViewModel(dispatchers.main) {

    private val _products = MutableLiveData<Result<ProductData>>()
    val products: LiveData<Result<ProductData>>
        get() = _products

    private val _deleteDiscount = MutableLiveData<Result<Boolean>>()
    val deleteDiscount: LiveData<Result<Boolean>>
        get() = _deleteDiscount

    private val _reserveProduct = MutableLiveData<Result<Boolean>>()
    val reserveProduct: LiveData<Result<Boolean>>
        get() = _reserveProduct

    private var totalProduct = 0
    private var selectedProduct : Product? = null
    private var isOnMultiSelectMode = false
    private var shouldDisableProductSelection = false
    private var requestId = ""
    private var selectedProductIds : MutableList<String> = mutableListOf()

    fun getSlashPriceProducts(
        page: Int,
        discountStatus: Int,
        keyword: String,
        isMultiSelectEnabled: Boolean,
        shouldDisableProductSelection: Boolean
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListUseCase.setRequestParams(
                    page = page,
                    status = discountStatus,
                    keyword = keyword
                )
                val response = getSlashPriceProductListUseCase.executeOnBackground()
                val mappedProduct = productMapper.map(response)
                val formattedProduct =
                    mappedProduct.map {
                        it.copy(
                            shouldDisplayCheckbox = isMultiSelectEnabled,
                            disableClick = shouldDisableProductSelection,
                            isCheckboxTicked = it.id in selectedProductIds
                        )
                    }
                Pair(response.getSlashPriceProductList.totalProduct, formattedProduct)
            }

            val (totalProduct, formattedProduct) = result
            val productData = ProductData(totalProduct, formattedProduct)
            _products.value = Success(productData)

        }, onError = {
            _products.value = Fail(it)
        })
    }

    fun deleteDiscount(discountStatusId: Int, productIds : List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                deleteDiscountUseCase.setParams(
                    discountStatusId = discountStatusId,
                    productIds = productIds
                )
                deleteDiscountUseCase.executeOnBackground()
            }

            _deleteDiscount.value = Success(result.doSlashPriceStop.responseHeader.success)

        }, onError = {
            _deleteDiscount.value = Fail(it)
        })
    }

    fun reserveProduct(requestId: String, productIds : List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val request = updateDiscountRequestMapper.map(requestId, productIds)
                reserveProductUseCase.setParams(request)
                reserveProductUseCase.executeOnBackground()
            }

            _reserveProduct.value = Success(result.doSlashPriceProductReservation.responseHeader.success)

        }, onError = {
            _reserveProduct.value = Fail(it)
        })
    }

    fun setTotalProduct(totalProduct : Int) {
        this.totalProduct = totalProduct
    }

    fun getTotalProduct(): Int {
        return this.totalProduct
    }

    fun setSelectedProduct(selectedProduct: Product) {
        this.selectedProduct = selectedProduct
    }

    fun getSelectedProduct() : Product?  {
        return selectedProduct
    }

    fun setInMultiSelectMode(isOnMultiSelectMode: Boolean) {
        this.isOnMultiSelectMode = isOnMultiSelectMode
    }

    fun isOnMultiSelectMode(): Boolean {
        return isOnMultiSelectMode
    }

    fun setDisableProductSelection(shouldDisableProductSelection: Boolean) {
        this.shouldDisableProductSelection = shouldDisableProductSelection
    }

    fun shouldDisableProductSelection(): Boolean {
        return shouldDisableProductSelection
    }

    fun enableMultiSelect(products : List<Product>) : List<Product> {
        return products.map { it.copy(shouldDisplayCheckbox = true) }
    }

    fun disableMultiSelect(products : List<Product>) : List<Product> {
        return products.map {
            it.copy(
                shouldDisplayCheckbox = false,
                isCheckboxTicked = false,
                disableClick = false
            )
        }
    }

    fun disableProducts(products: List<Product>): List<Product> {
        return products.map { product ->
            if (!product.isCheckboxTicked) {
                product.copy(disableClick = true)
            } else {
                product
            }
        }
    }

    fun enableProduct(products: List<Product>): List<Product> {
        return products.map { product ->
            if (!product.isCheckboxTicked) {
                product.copy(disableClick = false)
            } else {
                product
            }
        }
    }

    fun getSelectedProductIds() : List<String> {
        return selectedProductIds
    }

    fun getSelectedProductCount(): Int {
        return selectedProductIds.size
    }

    fun addProductToSelection(product : Product) {
        this.selectedProductIds.add(product.id)
    }

    fun removeProductFromSelection(product: Product) {
        this.selectedProductIds.remove(product.id)
    }

    fun removeAllProductFromSelection() {
        this.selectedProductIds.clear()
    }

    fun getRequestId() : String {
        return requestId
    }

    fun setRequestId(requestId : String) {
        this.requestId = requestId
    }
}