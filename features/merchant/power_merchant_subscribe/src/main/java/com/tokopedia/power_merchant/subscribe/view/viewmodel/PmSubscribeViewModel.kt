package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantFreeShippingStatus
import com.tokopedia.power_merchant.subscribe.view.model.ViewState
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.HideLoading
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.ShowLoading
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantRemoteConfig
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PmSubscribeViewModel @Inject constructor(
    private val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase,
    private val getShopFreeShippingStatusUseCase: GetShopFreeShippingStatusUseCase,
    private val remoteConfig: PowerMerchantRemoteConfig,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val getPmStatusInfoResult: LiveData<Result<PowerMerchantStatus>>
        get() = _getPmStatusInfoResult
    val getPmFreeShippingStatusResult: LiveData<Result<PowerMerchantFreeShippingStatus>>
        get() = _getPmFreeShippingStatusResult
    val onActivatePmSuccess: LiveData<Result<PowerMerchantFreeShippingStatus>>
        get() = _onActivatePmSuccess

    private val _viewState = MutableLiveData<ViewState>()
    private val _getPmStatusInfoResult = MutableLiveData<Result<PowerMerchantStatus>>()
    private val _getPmFreeShippingStatusResult = MutableLiveData<Result<PowerMerchantFreeShippingStatus>>()
    private val _onActivatePmSuccess = MutableLiveData<Result<PowerMerchantFreeShippingStatus>>()

    fun getPmStatusInfo(){
        showLoading()

        launchCatchError(block = {
            val shopId = userSession.shopId
            val freeShippingEnabled = remoteConfig.isFreeShippingEnabled()

            val response = withContext(dispatchers.io) {
                val params = GetPowerMerchantStatusUseCase.createRequestParams(shopId)
                getPowerMerchantStatusUseCase.getData(params)
            }

            val powerMerchantStatus = response.copy(freeShippingEnabled = freeShippingEnabled)
            if(powerMerchantStatus.kycUserProjectInfoPojo.kycProjectInfo != null) {
                _getPmStatusInfoResult.value = Success(powerMerchantStatus)
            } else {
                throw NullPointerException("kycProjectInfo must not be null")
            }
            hideLoading()
        }) {
            _getPmStatusInfoResult.value = Fail(it)
            hideLoading()
            Timber.d(it)
        }
    }

    fun getFreeShippingStatus() {
        val freeShippingEnabled = remoteConfig.isFreeShippingEnabled()

        if(freeShippingEnabled) {
            launchCatchError(block = {
                val freeShippingStatus = getShopFreeShippingStatus()
                _getPmFreeShippingStatusResult.value = Success(freeShippingStatus)
            }) {
                _getPmFreeShippingStatusResult.value = Fail(it)
            }
        }
    }

    fun onActivatePmSuccess() {
        showLoading()
        launchCatchError(block = {
            val freeShippingStatus = getShopFreeShippingStatus()
            _onActivatePmSuccess.value = Success(freeShippingStatus)
            hideLoading()
        })  {
            _onActivatePmSuccess.value = Fail(it)
            hideLoading()
        }
    }

    fun detachView() {
        getPowerMerchantStatusUseCase.unsubscribe()
    }

    private suspend fun getShopFreeShippingStatus(): PowerMerchantFreeShippingStatus {
        return withContext(dispatchers.io) {
            val userId = userSession.userId.toIntOrZero()
            val shopId = userSession.shopId.toIntOrZero()
            val pmStatus = _getPmStatusInfoResult.value as? Success<PowerMerchantStatus>
            val isPowerMerchantActive = pmStatus?.data?.goldGetPmOsStatus?.result?.data?.isPowerMerchantActive() ?: false
            val isPowerMerchantIdle = pmStatus?.data?.goldGetPmOsStatus?.result?.data?.isPowerMerchantIdle() ?: false

            val params = GetShopFreeShippingStatusUseCase.createRequestParams(userId, listOf(shopId))
            val freeShipping = getShopFreeShippingStatusUseCase.execute(params)

            val isActive = freeShipping.active
            val isEligible = freeShipping.isEligible()
            val isTransitionPeriod = remoteConfig.isTransitionPeriodEnabled()

            PowerMerchantFreeShippingStatus(
                isActive,
                isEligible,
                isTransitionPeriod,
                isPowerMerchantIdle,
                isPowerMerchantActive
            )
        }
    }

    private fun showLoading() {
        _viewState.value = ShowLoading
    }

    private fun hideLoading() {
        _viewState.value = HideLoading
    }
}