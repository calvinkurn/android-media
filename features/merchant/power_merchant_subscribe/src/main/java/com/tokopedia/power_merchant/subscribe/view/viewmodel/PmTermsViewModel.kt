package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.power_merchant.subscribe.common.coroutine.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.view.model.ViewState
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.HideLoading
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.ShowLoading
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PmTermsViewModel @Inject constructor(
    private val activatePowerMerchantUseCase: ActivatePowerMerchantUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val activatePmResult: LiveData<Result<Boolean>>
        get() = _activatePmResult

    private val _viewState = MutableLiveData<ViewState>()
    private val _activatePmResult = MutableLiveData<Result<Boolean>>()

    fun activatePowerMerchant() {
        showLoading()

        launchCatchError(block = {
            val isSuccess = withContext(dispatchers.io) {
                activatePowerMerchantUseCase.getData(RequestParams.EMPTY)
            }

            if (isSuccess) {
                _activatePmResult.value = Success(isSuccess)
            } else {
                throw RuntimeException()
            }

            hideLoading()
        }) {
            _activatePmResult.value = Fail(it)
            hideLoading()
            Timber.d(it)
        }
    }

    fun detachView() {
        activatePowerMerchantUseCase.unsubscribe()
    }

    private fun showLoading() {
        _viewState.value = ShowLoading
    }

    private fun hideLoading() {
        _viewState.value = HideLoading
    }
}