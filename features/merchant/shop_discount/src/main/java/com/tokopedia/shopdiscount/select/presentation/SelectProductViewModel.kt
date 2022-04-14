package com.tokopedia.shopdiscount.select.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.select.data.mapper.ReservableProductMapper
import com.tokopedia.shopdiscount.select.data.mapper.ReserveProductRequestMapper
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.select.domain.usecase.GetSlashPriceProductListToReserveUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SelectProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListToReserveUseCase: GetSlashPriceProductListToReserveUseCase,
    private val reserveProductUseCase : MutationDoSlashPriceProductReservationUseCase,
    private val reservableProductMapper: ReservableProductMapper,
    private val reserveProductRequestMapper: ReserveProductRequestMapper
) : BaseViewModel(dispatchers.main) {

    private val _products = MutableLiveData<Result<List<ReservableProduct>>>()
    val products: LiveData<Result<List<ReservableProduct>>>
        get() = _products

    private val _reserveProduct = MutableLiveData<Result<Boolean>>()
    val reserveProduct: LiveData<Result<Boolean>>
        get() = _reserveProduct

    private var requestId = ""
    private var shouldDisableProductSelection = false
    private var selectedProducts: MutableList<ReservableProduct> = mutableListOf()

    fun getProducts(
        requestId: String,
        page: Int,
        keyword: String,
        shouldDisableProductSelection: Boolean
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListToReserveUseCase.setParams(
                    requestId = requestId,
                    page = page,
                    keyword = keyword
                )
                val response = getSlashPriceProductListToReserveUseCase.executeOnBackground()
                val mappedProduct = reservableProductMapper.map(response)
                val selectedProductIds = getSelectedProductIds()
                mappedProduct.map {
                    it.copy(
                        disableClick = shouldDisableProductSelection && it.id !in selectedProductIds,
                        isCheckboxTicked = it.id in selectedProductIds
                    )
                }

            }

            _products.value = Success(result)

        }, onError = {
            _products.value = Fail(it)
        })
    }

    fun setDisableProductSelection(shouldDisableProductSelection: Boolean) {
        this.shouldDisableProductSelection = shouldDisableProductSelection
    }

    fun shouldDisableProductSelection(): Boolean {
        return shouldDisableProductSelection
    }

    fun disableProducts(products: List<ReservableProduct>): List<ReservableProduct> {
        return products.map { product ->
            if (!product.isCheckboxTicked) {
                product.copy(disableClick = true)
            } else {
                product
            }
        }
    }

    fun enableProduct(products: List<ReservableProduct>): List<ReservableProduct> {
        return products.map { product ->
            if (!product.isCheckboxTicked) {
                product.copy(disableClick = false)
            } else {
                product
            }
        }
    }

    private fun getSelectedProductIds(): List<String> {
        return selectedProducts.map { product -> product.id }
    }

    fun getSelectedProduct(): List<ReservableProduct> {
        return selectedProducts
    }

    fun addProductToSelection(product: ReservableProduct) {
        this.selectedProducts.add(product)
    }

    fun removeProductFromSelection(product: ReservableProduct) {
        this.selectedProducts.remove(product)
    }

    fun getRequestId() : String {
        return requestId
    }

    fun setRequestId(requestId : String) {
        this.requestId = requestId
    }

    fun reserveProduct(requestId: String, products : List<ReservableProduct>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val request = reserveProductRequestMapper.map(requestId, products)
                reserveProductUseCase.setParams(request)
                reserveProductUseCase.executeOnBackground()
            }

            _reserveProduct.value = Success(result.doSlashPriceProductReservation.responseHeader.success)

        }, onError = {
            _reserveProduct.value = Fail(it)
        })
    }
}