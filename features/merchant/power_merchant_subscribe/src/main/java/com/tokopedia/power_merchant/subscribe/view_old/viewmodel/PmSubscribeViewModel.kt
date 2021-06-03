package com.tokopedia.power_merchant.subscribe.view_old.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMSettingAndShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view_old.model.PMSettingAndShopInfoUiModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMStatusAndSettingUiModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantFreeShippingStatus
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.HideLoading
import com.tokopedia.power_merchant.subscribe.view_old.model.ViewState.ShowLoading
import com.tokopedia.power_merchant.subscribe.view_old.util.PowerMerchantRemoteConfig
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PmSubscribeViewModel @Inject constructor(
        private val getPMSettingAndShopInfoUseCase: GetPMSettingAndShopInfoUseCase,
        private val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase,
        private val getShopFreeShippingStatusUseCase: GetShopFreeShippingStatusUseCase,
        private val remoteConfig: PowerMerchantRemoteConfig,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val getPmStatusInfoResult: LiveData<Result<PMStatusAndSettingUiModel>>
        get() = _getPmStatusInfoResult
    val getPmFreeShippingStatusResult: LiveData<Result<PowerMerchantFreeShippingStatus>>
        get() = _getPmFreeShippingStatusResult
    val onActivatePmSuccess: LiveData<Result<PowerMerchantFreeShippingStatus>>
        get() = _onActivatePmSuccess

    private val _viewState = MutableLiveData<ViewState>()
    private val _getPmStatusInfoResult = MutableLiveData<Result<PMStatusAndSettingUiModel>>()
    private val _getPmFreeShippingStatusResult = MutableLiveData<Result<PowerMerchantFreeShippingStatus>>()
    private val _onActivatePmSuccess = MutableLiveData<Result<PowerMerchantFreeShippingStatus>>()

    fun getPmStatusInfo() {
        showLoading()
        launchCatchError(block = {
            val freeShippingEnabled = remoteConfig.isFreeShippingEnabled()

            val pmStatusAsync = async { getPowerMerchantStatus() }
            val settingAndShopInfoAsync = async { getSettingAndShopInfo() }
            val settingAndShopInfo = settingAndShopInfoAsync.await()
            val pmStatus = pmStatusAsync.await()

            when (settingAndShopInfo) {
                is Success -> {
                    if (settingAndShopInfo.data.pmSetting.periodeType == PeriodType.COMMUNICATION_PERIOD) {
                        when (pmStatus) {
                            is Success -> {
                                val powerMerchantStatus = pmStatus.data.copy(freeShippingEnabled = freeShippingEnabled)
                                if (powerMerchantStatus.kycUserProjectInfoPojo.kycProjectInfo != null) {
                                    val data = PMStatusAndSettingUiModel(
                                            pmStatus = pmStatus.data,
                                            pmSettingAndShopInfo = settingAndShopInfo.data
                                    )
                                    _getPmStatusInfoResult.value = Success(data)
                                } else {
                                    throw NullPointerException("kycProjectInfo must not be null")
                                }
                            }
                            is Fail -> throw pmStatus.throwable
                        }
                    } else {
                        val data = PMStatusAndSettingUiModel(
                                pmStatus = null,
                                pmSettingAndShopInfo = settingAndShopInfo.data
                        )
                        _getPmStatusInfoResult.value = Success(data)
                    }
                }
                is Fail -> throw settingAndShopInfo.throwable
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
        if (freeShippingEnabled) {
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
        }) {
            _onActivatePmSuccess.value = Fail(it)
            hideLoading()
        }
    }

    fun detachView() {
        getPowerMerchantStatusUseCase.unsubscribe()
    }

    private suspend fun getSettingAndShopInfo(): Result<PMSettingAndShopInfoUiModel> {
        return try {
            val result = withContext(dispatchers.io) {
                getPMSettingAndShopInfoUseCase.executeOnBackground()
            }
            Success(result)
        } catch (e: Exception) {
            return Fail(e)
        }
    }

    private suspend fun getPowerMerchantStatus(): Result<PowerMerchantStatus> {
        return try {
            val result = withContext(dispatchers.io) {
                val shopId = userSession.shopId
                val params = GetPowerMerchantStatusUseCase.createRequestParams(shopId)
                getPowerMerchantStatusUseCase.getData(params)
            }
            Success(result)
        } catch (e: Exception) {
            return Fail(e)
        }
    }

    private suspend fun getShopFreeShippingStatus(): PowerMerchantFreeShippingStatus {
        return withContext(dispatchers.io) {
            val userId = userSession.userId.toIntOrZero()
            val shopId = userSession.shopId.toIntOrZero()
            val pmStatus = _getPmStatusInfoResult.value as? Success<PowerMerchantStatus>
            val isPowerMerchantActive = pmStatus?.data?.goldGetPmOsStatus?.result?.data?.isPowerMerchantActive()
                    ?: false
            val isPowerMerchantIdle = pmStatus?.data?.goldGetPmOsStatus?.result?.data?.isPowerMerchantIdle()
                    ?: false

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