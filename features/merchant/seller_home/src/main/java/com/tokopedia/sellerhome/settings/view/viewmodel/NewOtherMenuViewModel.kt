package com.tokopedia.sellerhome.settings.view.viewmodel

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
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.domain.usecase.*
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
import com.tokopedia.sellerhome.domain.usecase.NewGetShopOperationalUseCase
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.OtherMenuErrorType
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

class NewOtherMenuViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase,
    private val getShopOperationalUseCase: NewGetShopOperationalUseCase,
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
    private val _shopPeriodType = MutableLiveData<Result<ShopInfoPeriodUiModel>>()

    private val _isFreeShippingActive = MutableLiveData<SettingResponseState<String>>()
    private val _shopBadgeLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _shopTotalFollowersLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _userShopInfoLiveData = MutableLiveData<SettingResponseState<ShopStatusUiModel>>()
    private val _shopOperationalLiveData = MutableLiveData<SettingResponseState<ShopOperationalData>>()
    private val _balanceInfoLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _kreditTopAdsLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _isTopAdsAutoTopupLiveData = MutableLiveData<Result<Boolean>>()

    val shopBadgeLiveData: LiveData<SettingResponseState<String>>
        get() = _shopBadgeLiveData
    val shopTotalFollowersLiveData: LiveData<SettingResponseState<String>>
        get() = _shopTotalFollowersLiveData
    val userShopInfoLiveData: LiveData<SettingResponseState<ShopStatusUiModel>>
        get() = _userShopInfoLiveData
    val shopOperationalLiveData: LiveData<SettingResponseState<ShopOperationalData>>
        get() = _shopOperationalLiveData
    val balanceInfoLiveData: LiveData<SettingResponseState<String>>
        get() = _balanceInfoLiveData
    val kreditTopAdsLiveData: LiveData<SettingResponseState<String>>
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

    private val _errorStateMap = MediatorLiveData<Map<OtherMenuErrorType, Boolean>>().apply {
        addSource(_shopBadgeLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuErrorType.Badge, it)
        }
        addSource(_shopTotalFollowersLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuErrorType.Followers, it)
        }
        addSource(_userShopInfoLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuErrorType.Status, it)
        }
        addSource(_shopOperationalLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuErrorType.Operational, it)
        }
        addSource(_balanceInfoLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuErrorType.Saldo, it)
        }
        addSource(_kreditTopAdsLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuErrorType.Topads, it)
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
    val isFreeShippingActive: LiveData<SettingResponseState<String>>
        get() = _isFreeShippingActive

    fun getAllOtherMenuData() {
        setErrorStateMapDefaultValue()
        getShopBadgeData()
        getShopTotalFollowersData()
        getUserShopInfoData()
        getFreeShippingStatusData()
        getShopOperationalData()
        getBalanceInfoData()
        getKreditTopAdsData()
        getFreeShippingStatusData()
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

    fun reloadErrorData() {
        _errorStateMap.value?.forEach{
            if (it.value) {
                when(it.key) {
                    OtherMenuErrorType.Badge -> getShopBadge()
                    OtherMenuErrorType.Followers -> getShopTotalFollowers()
                    OtherMenuErrorType.Status -> getUserShopInfo()
                    OtherMenuErrorType.Operational -> getShopOperational()
                    OtherMenuErrorType.Saldo -> getBalanceInfo()
                    else -> getKreditTopAds()
                }
            }
        }
    }

    fun getFreeShippingStatus() {
        _isFreeShippingActive.value = SettingResponseState.SettingLoading
        getFreeShippingStatusData()
    }

    fun getShopBadge() {
        _shopBadgeLiveData.value = SettingResponseState.SettingLoading
        getShopBadgeData()
    }

    fun getShopTotalFollowers() {
        _shopTotalFollowersLiveData.value = SettingResponseState.SettingLoading
        getShopTotalFollowersData()
    }

    fun getUserShopInfo() {
        _userShopInfoLiveData.value = SettingResponseState.SettingLoading
        getUserShopInfoData()
    }

    fun getShopOperational() {
        _shopOperationalLiveData.value = SettingResponseState.SettingLoading
        getShopOperationalData()
    }

    fun getBalanceInfo() {
        _balanceInfoLiveData.value = SettingResponseState.SettingLoading
        getBalanceInfoData()
    }

    fun getKreditTopAds() {
        _kreditTopAdsLiveData.value = SettingResponseState.SettingLoading
        getKreditTopAdsData()
    }

    private fun getFreeShippingStatusData() {

        // TODO: Check for remote config
        val freeShippingDisabled = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        val inTransitionPeriod = remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        launchCatchError(block = {
            val isFreeShippingActive = withContext(dispatcher.io) {
                val userId = userSession.userId.toIntOrZero()
                val shopId = userSession.shopId.toIntOrZero()
                val params = GetShopFreeShippingStatusUseCase.createRequestParams(userId, listOf(shopId))
                getShopFreeShippingInfoUseCase.execute(params).first().freeShipping.imgUrl
            }

            _isFreeShippingActive.value = SettingResponseState.SettingSuccess(isFreeShippingActive)
        }){}
    }

    private fun getShopBadgeData() {
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

    private fun getShopTotalFollowersData() {
        launchCatchError(
            block = {
                val totalFollowers = withContext(dispatcher.io) {
                    getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                    getShopTotalFollowersUseCase.executeOnBackground().let { shopFollowers ->
                        if (shopFollowers == Constant.INVALID_NUMBER_OF_FOLLOWERS) {
                            throw MessageErrorException(INVALID_FOLLOWERS_ERROR_MESSAGE)
                        } else {
                            shopFollowers
                        }
                    }
                }
                // TODO: Convert to formatted value
                _shopTotalFollowersLiveData.value = SettingResponseState.SettingSuccess(
                    totalFollowers.toString()
                )
            },
            onError = {
                _shopTotalFollowersLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getUserShopInfoData() {
        launchCatchError(
            block = {
                val userShopInfoWrapper = withContext(dispatcher.io) {
                    getUserShopInfoUseCase.params = GetUserShopInfoUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                    getUserShopInfoUseCase.executeOnBackground()
                }
                _userShopInfoLiveData.value = SettingResponseState.SettingSuccess(ShopStatusUiModel(userShopInfoWrapper, userSession))
            },
            onError = {
                _userShopInfoLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getShopOperationalData() {
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

    private fun getBalanceInfoData() {
        launchCatchError(
            block = {
                val balanceInfo = withContext(dispatcher.io) {
                        balanceInfoUseCase.executeOnBackground().totalBalance.orEmpty()
                }
                _balanceInfoLiveData.value = SettingResponseState.SettingSuccess(balanceInfo)
            },
            onError = {
                _balanceInfoLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getKreditTopAdsData() {
        launchCatchError(
            block = {
                val topAdsBalance = withContext(dispatcher.io) {
                    topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(userSession.shopId.toIntOrZero())
                    topAdsDashboardDepositUseCase.executeOnBackground().getCurrencyFormatted()
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
                OtherMenuErrorType.Badge to false,
                OtherMenuErrorType.Followers to false,
                OtherMenuErrorType.Status to false,
                OtherMenuErrorType.Operational to false,
                OtherMenuErrorType.Saldo to false,
                OtherMenuErrorType.Topads to false,
            )
        }
    }

    private fun SettingResponseState<*>.isError(): Boolean =
        this is SettingResponseState.SettingError

    private fun Map<OtherMenuErrorType, Boolean>.getUpdatedErrorMap(errorType: OtherMenuErrorType, state: SettingResponseState<*>): Map<OtherMenuErrorType, Boolean> {
        return mapValues {
            if (it.key == errorType) {
                state.isError()
            } else {
                it.value
            }
        }
    }

}