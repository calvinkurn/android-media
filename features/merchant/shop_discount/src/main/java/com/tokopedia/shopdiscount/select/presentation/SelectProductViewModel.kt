package com.tokopedia.shopdiscount.select.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.select.data.mapper.ReservableProductMapper
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
    private val reservableProductMapper: ReservableProductMapper
) : BaseViewModel(dispatchers.main) {

    private val _products = MutableLiveData<Result<List<ReservableProduct>>>()
    val products: LiveData<Result<List<ReservableProduct>>>
        get() = _products

    private var shouldDisableProductSelection = false
    private var selectedProductIds: MutableList<String> = mutableListOf()

    fun getProducts(
        page: Int,
        keyword: String,
        shouldDisableProductSelection: Boolean
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListToReserveUseCase.setParams(
                    page = page,
                    keyword = keyword
                )
                val response = getSlashPriceProductListToReserveUseCase.executeOnBackground()
                val mappedProduct = reservableProductMapper.map(response)

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

    fun getSelectedProductIds(): List<String> {
        return selectedProductIds
    }

    fun getSelectedProductCount(): Int {
        return selectedProductIds.size
    }

    fun addProductToSelection(product: ReservableProduct) {
        this.selectedProductIds.add(product.id)
    }

    fun removeProductFromSelection(product: ReservableProduct) {
        this.selectedProductIds.remove(product.id)
    }
}