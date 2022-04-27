package com.tokopedia.shopdiscount.select.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.select.data.mapper.ReservableProductMapper
import com.tokopedia.shopdiscount.select.data.mapper.ReserveProductRequestMapper
import com.tokopedia.shopdiscount.select.data.mapper.ShopBenefitMapper
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.select.domain.entity.ShopBenefit
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
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase,
    private val reservableProductMapper: ReservableProductMapper,
    private val reserveProductRequestMapper: ReserveProductRequestMapper,
    private val shopBenefitMapper: ShopBenefitMapper
) : BaseViewModel(dispatchers.main) {

    private val _products = MutableLiveData<Result<List<ReservableProduct>>>()
    val products: LiveData<Result<List<ReservableProduct>>>
        get() = _products

    private val _reserveProduct = MutableLiveData<Result<Boolean>>()
    val reserveProduct: LiveData<Result<Boolean>>
        get() = _reserveProduct

    private val _benefit = MutableLiveData<Result<ShopBenefit>>()
    val benefit: LiveData<Result<ShopBenefit>>
        get() = _benefit

    private var requestId = ""
    private var remainingQuota = 0
    private var shouldDisableProductSelection = false
    private var selectedProductIds: MutableList<String> = mutableListOf()

    fun getReservableProducts(
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


    fun getSellerBenefits() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceBenefitUseCase.setParams()
                val response = getSlashPriceBenefitUseCase.executeOnBackground()
                shopBenefitMapper.map(response)
            }
            _benefit.value = Success(result)
        }) {
            _benefit.value = Fail(it)
        }
    }


    fun setDisableProductSelection(shouldDisableProductSelection: Boolean) {
        this.shouldDisableProductSelection = shouldDisableProductSelection
    }

    fun shouldDisableProductSelection(): Boolean {
        return shouldDisableProductSelection
    }

    private fun getSelectedProductIds(): List<String> {
        return selectedProductIds
    }

    fun getSelectedProducts(): List<String> {
        return selectedProductIds
    }

    fun addProductToSelection(product: ReservableProduct) {
        this.selectedProductIds.add(product.id)
    }

    fun removeProductFromSelection(product: ReservableProduct) {
        this.selectedProductIds.remove(product.id)
    }

    fun setRequestId(requestId : String) {
        this.requestId = requestId
    }

    fun getRequestId() : String {
        return requestId
    }

    fun setRemainingQuota(remainingQuota : Int) {
        this.remainingQuota = remainingQuota
    }

    fun getRemainingQuota(): Int {
        return remainingQuota
    }

    fun reserveProduct(requestId: String, productIds : List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val request = reserveProductRequestMapper.map(requestId, productIds)
                reserveProductUseCase.setParams(request)
                reserveProductUseCase.executeOnBackground()
            }

            _reserveProduct.value = Success(result.doSlashPriceProductReservation.responseHeader.success)

        }, onError = {
            _reserveProduct.value = Fail(it)
        })
    }
}