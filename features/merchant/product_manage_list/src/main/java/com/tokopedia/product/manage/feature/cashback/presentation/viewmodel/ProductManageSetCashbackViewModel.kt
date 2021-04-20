package com.tokopedia.product.manage.feature.cashback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase.Companion.CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase.Companion.CASHBACK_SUCCESS_CODE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageSetCashbackViewModel @Inject constructor(
        private val setCashbackUseCase: SetCashbackUseCase,
        private val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {

    val setCashbackResult: LiveData<Result<SetCashbackResult>>
        get() = _setCashbackResult

    private val _setCashbackResult = MutableLiveData<Result<SetCashbackResult>>()

    fun setCashback(productId: String, productName: String, cashback: Int) {
        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                setCashbackUseCase.setParams(productId.toIntOrZero(), cashback, false)
                setCashbackUseCase.executeOnBackground()
            }
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