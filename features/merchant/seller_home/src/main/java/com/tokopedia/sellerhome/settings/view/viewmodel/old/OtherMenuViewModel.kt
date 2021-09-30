package com.tokopedia.sellerhome.settings.view.viewmodel.old

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.seller.menu.common.view.uimodel.base.BalanceType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
import com.tokopedia.sellerhome.domain.usecase.old.GetShopOperationalUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.OtherMenuDataType
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

        private const val INVALID_FOLLOWERS_ERROR_MESSAGE =  "Shop followers value is invalid"
    }

    private val _isToasterAlreadyShown = NonNullLiveData(false)
    private val _isStatusBarInitialState = MutableLiveData<Boolean>().apply { value = true }
    private val _isFreeShippingActive = MutableLiveData<Boolean>()
    private val _shopPeriodType = MutableLiveData<Result<ShopInfoPeriodUiModel>>()

    private val _shopBadgeLiveData = MutableLiveData<SettingResponseState<ShopBadgeUiModel>>()
    private val _shopTotalFollowersLiveData = MutableLiveData<SettingResponseState<ShopFollowersUiModel>>()
    private val _userShopInfoLiveData = MutableLiveData<SettingResponseState<ShopStatusUiModel>>()
    private val _shopOperationalLiveData = MutableLiveData<SettingResponseState<ShopOperationalUiModel>>()
    private val _balanceInfoLiveData = MutableLiveData<SettingResponseState<BalanceUiModel>>()
    private val _kreditTopAdsLiveData = MutableLiveData<SettingResponseState<TopadsBalanceUiModel>>()
    private val _isTopAdsAutoTopupLiveData = MutableLiveData<Result<Boolean>>()

    val shopBadgeLiveData: LiveData<SettingResponseState<ShopBadgeUiModel>>
        get() = _shopBadgeLiveData
    val shopTotalFollowersLiveData: LiveData<SettingResponseState<ShopFollowersUiModel>>
        get() = _shopTotalFollowersLiveData
    val userShopInfoLiveData: LiveData<SettingResponseState<ShopStatusUiModel>>
        get() = _userShopInfoLiveData
    val shopOperationalLiveData: LiveData<SettingResponseState<ShopOperationalUiModel>>
        get() = _shopOperationalLiveData
    val balanceInfoLiveData: LiveData<SettingResponseState<BalanceUiModel>>
        get() = _balanceInfoLiveData
    val kreditTopAdsLiveData: LiveData<SettingResponseState<TopadsBalanceUiModel>>
        get() = _kreditTopAdsLiveData
    val isTopAdsAutoTopupLiveData: LiveData<Result<Boolean>>
        get() = _isTopAdsAutoTopupLiveData

    private val _shopBadgeFollowersErrorLiveData = MutableLiveData<Boolean>()
    val shopBadgeFollowersErrorLiveData: LiveData<Boolean>
        get() = _shopBadgeFollowersErrorLiveData
    private val _shopBadgeFollowersShimmerLiveData = MediatorLiveData<Boolean>().apply {
        addSource(_shopBadgeLiveData) { state ->
            val shouldShowShimmer = state is SettingResponseState.SettingLoading &&
                    _shopTotalFollowersLiveData.value is SettingResponseState.SettingLoading
            val shouldShowError = state is SettingResponseState.SettingError &&
                    _shopTotalFollowersLiveData.value is SettingResponseState.SettingError
            value = shouldShowShimmer
            _shopBadgeFollowersErrorLiveData.value = shouldShowError
        }
        addSource(_shopTotalFollowersLiveData) { state ->
            val shouldShowShimmer = state is SettingResponseState.SettingLoading &&
                    _shopBadgeLiveData.value is SettingResponseState.SettingLoading
            val shouldShowError = state is SettingResponseState.SettingError &&
                    _shopBadgeLiveData.value is SettingResponseState.SettingError
            value = shouldShowShimmer
            _shopBadgeFollowersErrorLiveData.value = shouldShowError
        }
    }
    val shopBadgeFollowersShimmerLiveData: LiveData<Boolean>
        get() = _shopBadgeFollowersShimmerLiveData

    private val _errorStateMap = MediatorLiveData<Map<OtherMenuDataType, Boolean>>().apply {
        addSource(_shopBadgeLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Badge, it)
        }
        addSource(_shopTotalFollowersLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Followers, it)
        }
        addSource(_userShopInfoLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Status, it)
        }
        addSource(_shopOperationalLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Operational, it)
        }
        addSource(_balanceInfoLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Saldo, it)
        }
        addSource(_kreditTopAdsLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Topads, it)
        }
    }

    private val _shouldShowAllError = MutableLiveData<Boolean>()
    val shouldShowAllError: LiveData<Boolean>
        get() = _shouldShowAllError
    private val _shouldShowMultipleErrorToaster = MediatorLiveData<Boolean>().apply {
        addSource(_errorStateMap) { map ->
            val errorCounts = map?.count { it.value }.orZero()
            if (errorCounts < map?.count().orZero()) {
                val shouldShowMultipleErrorToaster = errorCounts >= 2 && _hasShownMultipleErrorToaster.value == false
                value = shouldShowMultipleErrorToaster
                _hasShownMultipleErrorToaster.value = shouldShowMultipleErrorToaster
                _shouldShowAllError.value = false
            } else {
                value = false
                _shouldShowAllError.value = true
            }
        }
    }
    val shouldShowMultipleErrorToaster: LiveData<Boolean>
        get() = _shouldShowMultipleErrorToaster
    private val _hasShownMultipleErrorToaster = MutableLiveData(false)

    val shopPeriodType: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopPeriodType
    val isStatusBarInitialState: LiveData<Boolean>
        get() = _isStatusBarInitialState
    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown
    val isFreeShippingActive: LiveData<Boolean>
        get() = _isFreeShippingActive

    fun getAllOtherMenuData() {
        setErrorStateMapDefaultValue()
        getShopBadge()
        getShopTotalFollowers()
        getUserShopInfo()
        getFreeShippingStatus()
        getShopOperational()
        getBalanceInfo()
        getKreditTopAds()
        getIsTopAdsAutoTopup()
    }

    fun onReloadErrorData() {
        _hasShownMultipleErrorToaster.value = false
    }

    fun onCheckDelayErrorResponseTrigger() {
        launch(coroutineContext) {
            _isToasterAlreadyShown.value.let { isToasterAlreadyShown ->
                if (!isToasterAlreadyShown){
                    _isToasterAlreadyShown.value = true
                    delay(DELAY_TIME)
                    _isToasterAlreadyShown.value = false
                }
            }
        }
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
                val userId = userSession.userId.toLongOrZero()
                val shopId = userSession.shopId.toLongOrZero()
                val params = GetShopFreeShippingStatusUseCase.createRequestParams(userId, listOf(shopId))
                getShopFreeShippingInfoUseCase.execute(params).first().freeShipping.isActive
            }

            _isFreeShippingActive.value = isFreeShippingActive
        }){}
    }

    fun reloadErrorData() {
        _errorStateMap.value?.forEach{
            if (it.value) {
                when(it.key) {
                    OtherMenuDataType.Badge -> getShopBadge()
                    OtherMenuDataType.Followers -> getShopTotalFollowers()
                    OtherMenuDataType.Status -> getUserShopInfo()
                    OtherMenuDataType.Operational -> getShopOperational()
                    OtherMenuDataType.Saldo -> getBalanceInfo()
                    else -> getKreditTopAds()
                }
            }
        }
    }

    fun getShopBadgeAndFollowers() {
        if (_shouldShowAllError.value == true) {
            getAllOtherMenuData()
        } else {
            getShopBadge()
            getShopTotalFollowers()
        }
    }

    fun getShopBadge() {
        _shopBadgeLiveData.value = SettingResponseState.SettingLoading
        launchCatchError(
                block = {
                    val badgeUrl = withContext(dispatcher.io) {
                        getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(userSession.shopId.toLongOrZero())
                        getShopBadgeUseCase.executeOnBackground()
                    }
                    _shopBadgeLiveData.value = SettingResponseState.SettingSuccess(ShopBadgeUiModel(badgeUrl))
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
                        getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(userSession.shopId.toLongOrZero())
                        getShopTotalFollowersUseCase.executeOnBackground().let { shopFollowers ->
                            if (shopFollowers == Constant.INVALID_NUMBER_OF_FOLLOWERS) {
                                throw MessageErrorException(INVALID_FOLLOWERS_ERROR_MESSAGE)
                            } else {
                                shopFollowers
                            }
                        }
                    }
                    _shopTotalFollowersLiveData.value = SettingResponseState.SettingSuccess(ShopFollowersUiModel(totalFollowers))
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
                        getUserShopInfoUseCase.params = GetUserShopInfoUseCase.createRequestParams(userSession.shopId.toLongOrZero())
                        getUserShopInfoUseCase.executeOnBackground()
                    }
                    _userShopInfoLiveData.value = SettingResponseState.SettingSuccess(ShopStatusUiModel(userShopInfoWrapper, userSession))
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
                        topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(userSession.shopId.toLongOrZero())
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

    private fun getIsTopAdsAutoTopup() {
        launchCatchError(
                block = {
                    val isTopAdsAutoTopup = withContext(dispatcher.io) {
                        topAdsAutoTopupUseCase.params = TopAdsAutoTopupUseCase.createRequestParams(userSession.shopId)
                        topAdsAutoTopupUseCase.executeOnBackground()
                    }
                    _isTopAdsAutoTopupLiveData.value = Success(isTopAdsAutoTopup)
                },
                onError = {
                    _isTopAdsAutoTopupLiveData.value = Fail(it)
                }
        )
    }

    fun setErrorStateMapDefaultValue() {
        if (_errorStateMap.value == null) {
            _errorStateMap.value = mutableMapOf(
                    OtherMenuDataType.Badge to false,
                    OtherMenuDataType.Followers to false,
                    OtherMenuDataType.Status to false,
                    OtherMenuDataType.Operational to false,
                    OtherMenuDataType.Saldo to false,
                    OtherMenuDataType.Topads to false,
            )
        }
    }

    private fun SettingResponseState<*>.isError(): Boolean =
            this is SettingResponseState.SettingError

    private fun Map<OtherMenuDataType, Boolean>.getUpdatedErrorMap(dataType: OtherMenuDataType, state: SettingResponseState<*>): Map<OtherMenuDataType, Boolean> {
        return mapValues {
            if (it.key == dataType) {
                state.isError()
            } else {
                it.value
            }
        }
    }

}

