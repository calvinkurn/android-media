package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.power_merchant.subscribe.common.coroutine.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.view.model.ViewState
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PmSubscribeViewModel @Inject constructor(
    private val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val getPmStatusInfoResult: LiveData<Result<PowerMerchantStatus>>
        get() = _getPmStatusInfoResult

    private val _viewState = MutableLiveData<ViewState>()
    private val _getPmStatusInfoResult = MutableLiveData<Result<PowerMerchantStatus>>()

    fun getPmStatusInfo(shopId: String){
        showLoading()

        launchCatchError(block = {
            val powerMerchantStatus = withContext(dispatchers.io) {
                val params = GetPowerMerchantStatusUseCase.createRequestParams(shopId)
                getPowerMerchantStatusUseCase.getData(params)
            }

            _getPmStatusInfoResult.value = Success(powerMerchantStatus)
            hideLoading()
        }) {
            _getPmStatusInfoResult.value = Fail(it)
            hideLoading()
            Timber.d(it)
        }

    }

    fun detachView() {
        getPowerMerchantStatusUseCase.unsubscribe()
    }

    private fun showLoading() {
        _viewState.value = ShowLoading
    }

    private fun hideLoading() {
        _viewState.value = HideLoading
    }
}