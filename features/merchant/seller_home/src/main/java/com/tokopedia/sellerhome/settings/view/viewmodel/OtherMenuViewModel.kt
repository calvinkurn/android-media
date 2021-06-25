package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.BalanceType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
import com.tokopedia.seller.menu.common.view.uimodel.base.partialresponse.PartialSettingSuccessInfoType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.BalanceUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.TopadsBalanceUiModel
import com.tokopedia.sellerhome.domain.usecase.GetShopOperationalUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.ShopOperationalUiModel
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OtherMenuViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val getAllShopInfoUseCase: GetAllShopInfoUseCase,
        private val getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase,
        private val getShopOperationalUseCase: GetShopOperationalUseCase,
        private val getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase,
        private val balanceInfoUseCase: BalanceInfoUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
        private val getUserShopInfoUseCase: GetUserShopInfoUseCase,
        private val topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
        private val topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
        private val userSession: UserSessionInterface,
        private val remoteConfig: FirebaseRemoteConfigImpl
): BaseViewModel(dispatcher.main) {

    companion object {
        private const val DELAY_TIME = 5000L

        private const val CUSTOM_ERROR_EXCEPTION_MESSAGE = "both shop info and topads response are failed"

        private const val ERROR_BADGE = 1
        private const val ERROR_FOLLOWERS = 2
        private const val ERROR_STATUS = 3
        private const val ERROR_OPERATIONAL = 4
        private const val ERROR_SALDO = 5
        private const val ERROR_TOPADS = 6
    }

    private val _settingShopInfoLiveData = MutableLiveData<Result<SettingShopInfoUiModel>>()
    private val _isToasterAlreadyShown = NonNullLiveData(false)
    private val _isStatusBarInitialState = MutableLiveData<Boolean>().apply { value = true }
    private val _isFreeShippingActive = MutableLiveData<Boolean>()
    private val _shopPeriodType = MutableLiveData<Result<ShopInfoPeriodUiModel>>()
    private val _shopOperational = MutableLiveData<Result<ShopOperationalUiModel>>()

    private val _shopBadgeLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _shopTotalFollowersLiveData = MutableLiveData<SettingResponseState<Long>>()
    private val _userShopInfoLiveData = MutableLiveData<SettingResponseState<UserShopInfoWrapper>>()
    private val _shopOperationalLiveData = MutableLiveData<SettingResponseState<ShopOperationalUiModel>>()
    private val _balanceInfoLiveData = MutableLiveData<SettingResponseState<BalanceUiModel>>()
    private val _kreditTopAdsLiveData = MutableLiveData<SettingResponseState<TopadsBalanceUiModel>>()
    private val _isTopAdsAutoTopupLiveData = MutableLiveData<SettingResponseState<Boolean>>()

    private val _shopBadgeFollowersShimmerLiveData = MediatorLiveData<Boolean>().apply {
        addSource(_shopBadgeLiveData) { state ->
            val shouldShowShimmer = state is SettingResponseState.SettingLoading &&
                    _shopTotalFollowersLiveData.value is SettingResponseState.SettingLoading
            value = shouldShowShimmer
        }
        addSource(_shopTotalFollowersLiveData) { state ->
            val shouldShowShimmer = state is SettingResponseState.SettingLoading &&
                    _shopBadgeLiveData.value is SettingResponseState.SettingLoading
            value = shouldShowShimmer
        }
    }

    private val _errorStateMap = MediatorLiveData<Map<Int, Boolean>>().apply {
        value = mapOf(
                ERROR_BADGE to false,
                ERROR_FOLLOWERS to false,
                ERROR_STATUS to false,
                ERROR_OPERATIONAL to false,
                ERROR_SALDO to false,
                ERROR_TOPADS to false
        )
    }

    val shopPeriodType: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopPeriodType
    val settingShopInfoLiveData: LiveData<Result<SettingShopInfoUiModel>>
        get() = _settingShopInfoLiveData
    val isStatusBarInitialState: LiveData<Boolean>
        get() = _isStatusBarInitialState
    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown
    val isFreeShippingActive: LiveData<Boolean>
        get() = _isFreeShippingActive

    val shopBadgeLiveData: LiveData<SettingResponseState<String>>
        get() = _shopBadgeLiveData
    val shopTotalFollowersLiveData: LiveData<SettingResponseState<Long>>
        get() = _shopTotalFollowersLiveData
    val shopBadgeFollowersShimmerLiveData: LiveData<Boolean>
        get() = _shopBadgeFollowersShimmerLiveData
    val userShopInfoLiveData: LiveData<SettingResponseState<UserShopInfoWrapper>>
        get() = _userShopInfoLiveData
    val shopOperationalLiveData: LiveData<SettingResponseState<ShopOperationalUiModel>>
        get() = _shopOperationalLiveData
    val balanceInfoLiveData: LiveData<SettingResponseState<BalanceUiModel>>
        get() = _balanceInfoLiveData
    val kreditTopAdsLiveData: LiveData<SettingResponseState<TopadsBalanceUiModel>>
        get() = _kreditTopAdsLiveData
    val isTopAdsAutoTopupLiveData: LiveData<SettingResponseState<Boolean>>
        get() = _isTopAdsAutoTopupLiveData

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

    fun getShopPeriodType() {
        launchCatchError(block = {
            val periodData = withContext(dispatcher.io) {
                getShopInfoPeriodUseCase.requestParams = GetShopInfoPeriodUseCase.createParams(userSession.shopId.toLongOrZero())
                getShopInfoPeriodUseCase.executeOnBackground()
            }
            _shopPeriodType.value = Success(periodData)
        }, onError = {
            _shopPeriodType.value = Fail(it)
        })
    }

    fun getFreeShippingStatus() {
        val freeShippingDisabled = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        val inTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        if(freeShippingDisabled || inTransitionPeriod) return

        launchCatchError(block = {
            val isFreeShippingActive = withContext(dispatcher.io) {
                val userId = userSession.userId.toIntOrZero()
                val shopId = userSession.shopId.toIntOrZero()
                val params = GetShopFreeShippingStatusUseCase.createRequestParams(userId, listOf(shopId))
                getShopFreeShippingInfoUseCase.execute(params).first().freeShipping.isActive
            }

            _isFreeShippingActive.value = isFreeShippingActive
        }){}
    }

    fun getShopBadge() {
        _shopBadgeLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val badgeUrl = withContext(dispatcher.io) {
                        getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                        getShopBadgeUseCase.executeOnBackground()
                    }
                    _shopBadgeLiveData.value = SettingResponseState.SettingSuccess(badgeUrl)
                },
                onError = {
                    _shopBadgeLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }

    fun getShopTotalFollowers() {
        _shopTotalFollowersLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val totalFollowers = withContext(dispatcher.io) {
                        getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                        getShopTotalFollowersUseCase.executeOnBackground()
                    }
                    _shopTotalFollowersLiveData.value = SettingResponseState.SettingSuccess(totalFollowers)
                },
                onError = {
                    _shopTotalFollowersLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }

    fun getUserShopInfo() {
        _userShopInfoLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val userShopInfoWrapper = withContext(dispatcher.io) {
                        getUserShopInfoUseCase.params = GetUserShopInfoUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                        getUserShopInfoUseCase.executeOnBackground()
                    }
                    _userShopInfoLiveData.value = SettingResponseState.SettingSuccess(userShopInfoWrapper)
                },
                onError = {
                    _userShopInfoLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }

    fun getShopOperational() {
        _shopOperationalLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val shopOperational = withContext(dispatcher.io) {
                        getShopOperationalUseCase.executeOnBackground()
                    }
                    _shopOperationalLiveData.value = SettingResponseState.SettingSuccess(shopOperational)
                },
                onError = {
                    _shopOperationalLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }


    fun getBalanceInfo() {
        _balanceInfoLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val balanceInfo = withContext(dispatcher.io) {
                        BalanceUiModel(
                                BalanceType.SALDO,
                                balanceInfoUseCase.executeOnBackground().totalBalance.orEmpty())
                    }
                    _balanceInfoLiveData.value = SettingResponseState.SettingSuccess(balanceInfo)
                },
                onError = {
                    _balanceInfoLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }

    fun getKreditTopAds() {
        _kreditTopAdsLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val topAdsBalance = withContext(dispatcher.io) {
                        topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                        val formattedTopAdsBalance = topAdsDashboardDepositUseCase.executeOnBackground().getCurrencyFormatted()
                        TopadsBalanceUiModel(formattedTopAdsBalance)
                    }
                    _kreditTopAdsLiveData.value = SettingResponseState.SettingSuccess(topAdsBalance)
                },
                onError = {
                    _kreditTopAdsLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }

    fun getIsTopAdsAutoTopup() {
        launchCatchError(
                block = {
                    val isTopAdsAutoTopup = withContext(dispatcher.io) {
                        topAdsAutoTopupUseCase.params = TopAdsAutoTopupUseCase.createRequestParams(userSession.shopId)
                        topAdsAutoTopupUseCase.executeOnBackground()
                    }
                    _isTopAdsAutoTopupLiveData.value = SettingResponseState.SettingSuccess(isTopAdsAutoTopup)
                },
                onError = {
                    _isTopAdsAutoTopupLiveData.value = SettingResponseState.SettingError(it)
                }
        )
    }

    private fun getAllShopInfoData() {
        launchCatchError(block = {
            _settingShopInfoLiveData.value = Success(
                    withContext(dispatcher.io) {
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

