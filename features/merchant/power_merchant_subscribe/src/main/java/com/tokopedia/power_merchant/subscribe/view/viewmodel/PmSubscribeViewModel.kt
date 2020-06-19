package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.power_merchant.subscribe.common.coroutine.CoroutineDispatchers
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantFreeShippingTracker
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment.Companion.MINIMUM_SCORE_ACTIVATE_IDLE
import com.tokopedia.power_merchant.subscribe.view.model.PowerMerchantFreeShippingStatus
import com.tokopedia.power_merchant.subscribe.view.model.ViewState
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.HideLoading
import com.tokopedia.power_merchant.subscribe.view.model.ViewState.ShowLoading
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
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
    private val remoteConfig: RemoteConfig,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val getPmStatusInfoResult: LiveData<Result<PowerMerchantStatus>>
        get() = _getPmStatusInfoResult
    val getPmFreeShippingStatusResult: LiveData<Result<PowerMerchantFreeShippingStatus>>
        get() = _getPmFreeShippingStatusResult
    val onActivatePmSuccess: LiveData<PowerMerchantFreeShippingStatus>
        get() = _onActivatePmSuccess

    private val _viewState = MutableLiveData<ViewState>()
    private val _getPmStatusInfoResult = MutableLiveData<Result<PowerMerchantStatus>>()
    private val _getPmFreeShippingStatusResult = MutableLiveData<Result<PowerMerchantFreeShippingStatus>>()
    private val _onActivatePmSuccess = MutableLiveData<PowerMerchantFreeShippingStatus>()

    private var freeShippingImpressionTracked = false

    fun getPmStatusInfo(){
        showLoading()

        launchCatchError(block = {
            val shopId = userSession.shopId
            val freeShippingEnabled = !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED)

            val response = withContext(dispatchers.io) {
                val params = GetPowerMerchantStatusUseCase.createRequestParams(shopId)
                getPowerMerchantStatusUseCase.getData(params)
            }

            val powerMerchantStatus = response.copy(freeShippingEnabled = freeShippingEnabled)
            _getPmStatusInfoResult.value = Success(powerMerchantStatus)
            hideLoading()
        }) {
            _getPmStatusInfoResult.value = Fail(it)
            hideLoading()
            Timber.d(it)
        }
    }

    fun getFreeShippingStatus() {
        val freeShippingEnabled = !remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED)

        if(freeShippingEnabled) {
            launchCatchError(block = {
                val shopId = userSession.shopId.toInt()
                val pmStatus = _getPmStatusInfoResult.value as? Success<PowerMerchantStatus>
                val shopScore = pmStatus?.data?.shopScore?.data?.value.orZero()

                val freeShippingStatus = withContext(dispatchers.io) {
                    val params = GetShopFreeShippingStatusUseCase.createRequestParams(listOf(shopId))
                    val freeShipping = getShopFreeShippingStatusUseCase.execute(params)

                    val isActive = freeShipping.active
                    val isEligible = freeShipping.eligible
                    val isTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
                    val isShopScoreEligible = shopScore >= MINIMUM_SCORE_ACTIVATE_IDLE

                    PowerMerchantFreeShippingStatus(
                        isActive,
                        isEligible,
                        isTransitionPeriod,
                        isShopScoreEligible
                    )
                }
                _getPmFreeShippingStatusResult.value = Success(freeShippingStatus)
            }) {
                _getPmFreeShippingStatusResult.value = Fail(it)
            }
        }
    }

    fun onActivatePmSuccess() {
        val getFreeShippingResult = _getPmFreeShippingStatusResult.value
            as? Success<PowerMerchantFreeShippingStatus>

        val freeShippingStatus = getFreeShippingResult?.data
            ?: PowerMerchantFreeShippingStatus(
                isActive = false,
                isEligible = false,
                isTransitionPeriod = false,
                isShopScoreEligible = false
            )

        _onActivatePmSuccess.value = freeShippingStatus
    }

    fun trackFreeShippingImpression() {
        if(!freeShippingImpressionTracked) {
            val isTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
            PowerMerchantFreeShippingTracker.sendImpressionFreeShipping(userSession, isTransitionPeriod)
            freeShippingImpressionTracked = true
        }
    }

    fun trackFreeShippingClick() {
        val isTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        PowerMerchantFreeShippingTracker.sendClickFreeShipping(userSession, isTransitionPeriod)
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