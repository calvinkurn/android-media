package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
import com.tokopedia.seller.menu.common.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class OtherMenuViewModel @Inject constructor(
    @Named("Main") dispatcher: CoroutineDispatcher,
    private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
    private val getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase,
    private val userSession: UserSessionInterface,
    private val remoteConfig: FirebaseRemoteConfigImpl
): BaseViewModel(dispatcher) {

    companion object {
        private const val DELAY_TIME = 5000L

        private const val CUSTOM_ERROR_EXCEPTION_MESSAGE = "both shop info and topads response are failed"
    }

    private val _settingShopInfoLiveData = MutableLiveData<Result<SettingShopInfoUiModel>>()
    private val _isToasterAlreadyShown = NonNullLiveData(false)
    private val _isStatusBarInitialState = MutableLiveData<Boolean>().apply { value = true }
    private val _isFreeShippingActive = MutableLiveData<Boolean>()

    val settingShopInfoLiveData: LiveData<Result<SettingShopInfoUiModel>>
        get() = _settingShopInfoLiveData
    val isStatusBarInitialState: LiveData<Boolean>
        get() = _isStatusBarInitialState
    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown
    val isFreeShippingActive: LiveData<Boolean>
        get() = _isFreeShippingActive

    fun getAllSettingShopInfo(isToasterRetry: Boolean = false) {
        if (isToasterRetry) {
            launch(coroutineContext) {
                checkDelayErrorResponseTrigger()
            }
        }
        getAllShopInfoData()
    }

    fun setIsStatusBarInitialState(isInitialState: Boolean) {
        _isStatusBarInitialState.value = isInitialState
    }

    fun getFreeShippingStatus() {
        val freeShippingDisabled = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        val inTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        if(freeShippingDisabled || inTransitionPeriod) return

        launchCatchError(block = {
            val isFreeShippingActive = withContext(Dispatchers.IO) {
                val userId = userSession.userId.toIntOrZero()
                val shopId = userSession.shopId.toIntOrZero()
                val params = GetShopFreeShippingStatusUseCase.createRequestParams(userId, listOf(shopId))
                getShopFreeShippingInfoUseCase.execute(params).first().freeShipping.isActive
            }

            _isFreeShippingActive.value = isFreeShippingActive
        }){}
    }

    private fun getAllShopInfoData() {
        launchCatchError(block = {
            _settingShopInfoLiveData.value = Success(
                    withContext(Dispatchers.IO) {
                        with(getAllShopInfoUseCase.executeOnBackground()) {
                            if (first is PartialSettingSuccessInfoType || second is PartialSettingSuccessInfoType) {
                                SettingShopInfoUiModel(first, second, userSession)
                            } else {
                                throw MessageErrorException(CUSTOM_ERROR_EXCEPTION_MESSAGE)
                            }
                        }
                    }
            )
        }, onError = {
            _settingShopInfoLiveData.value = Fail(it)
        })
    }

    private suspend fun checkDelayErrorResponseTrigger() {
        _isToasterAlreadyShown.value.let { isToasterAlreadyShown ->
            if (!isToasterAlreadyShown){
                _isToasterAlreadyShown.value = true
                delay(DELAY_TIME)
                _isToasterAlreadyShown.value = false
            }
        }
    }

}

