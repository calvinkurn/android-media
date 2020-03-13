package com.tokopedia.product.manage.feature.cashback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase.Companion.CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase.Companion.CASHBACK_SUCCESS_CODE
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductManageSetCashbackViewModel @Inject constructor(private val setCashbackUseCase: SetCashbackUseCase, dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val product: LiveData<ProductViewModel>
        get() = _product
    val setCashbackResult: LiveData<Result<SetCashbackResult>>
        get() = _setCashbackResult

    private val _product = MutableLiveData<ProductViewModel>()
    private val _setCashbackResult = MutableLiveData<Result<SetCashbackResult>>()

    fun updateProduct(product: ProductViewModel) {
        _product.postValue(product)
    }

    fun updateCashback(cashback: Int) {
        val updatedProduct = _product.value?.copy(cashBack = cashback)
        _product.value = updatedProduct
    }

    fun setCashback(productId: String, productName: String, cashback: Int) {
        setCashbackUseCase.params = SetCashbackUseCase.createRequestParams(productId.toIntOrZero(), cashback, false)
        launchCatchError(block = {
            val result = setCashbackUseCase.executeOnBackground()
            when(result.goldSetProductCashback.header.errorCode) {
                CASHBACK_SUCCESS_CODE -> _setCashbackResult.postValue(Success(SetCashbackResult(productId = productId, cashback = cashback, productName = productName)))
                CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE -> _setCashbackResult.postValue(Success(SetCashbackResult(limitExceeded = true)))
                else -> _setCashbackResult.postValue(Fail(SetCashbackResult(productId = productId, productName = productName, cashback = cashback)))
            }
        }) {
            _setCashbackResult.postValue(Fail(SetCashbackResult()))
        }
    }

}