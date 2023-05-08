package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopCreatedInfoUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.domain.usecase.BalanceInfoUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopBadgeUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.seller.menu.common.domain.usecase.GetUserShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopStatusUiModel
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
import com.tokopedia.sellerhome.domain.usecase.GetNewPromotionUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopOperationalUseCase
import com.tokopedia.sellerhome.domain.usecase.GetTotalTokoMemberUseCase
import com.tokopedia.sellerhome.domain.usecase.ShareInfoOtherUseCase
import com.tokopedia.sellerhome.domain.usecase.TopAdsAutoTopupUseCase
import com.tokopedia.sellerhome.domain.usecase.TopAdsDashboardDepositUseCase
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.OtherMenuShopShareData
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.OtherMenuDataType
import com.tokopedia.shop.common.graphql.domain.usecase.GetTokoPlusBadgeUseCase
import com.tokopedia.shop.common.view.model.TokoPlusBadgeUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OtherMenuViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getTokoPlusBadgeUseCase: GetTokoPlusBadgeUseCase,
    private val getShopOperationalUseCase: GetShopOperationalUseCase,
    private val getShopCreatedInfoUseCase: GetShopCreatedInfoUseCase,
    private val balanceInfoUseCase: BalanceInfoUseCase,
    private val getShopBadgeUseCase: GetShopBadgeUseCase,
    private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase,
    private val getShopTotalTokoMembersUseCase: GetTotalTokoMemberUseCase,
    private val getUserShopInfoUseCase: GetUserShopInfoUseCase,
    private val topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
    private val topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
    private val shopShareInfoUseCase: ShareInfoOtherUseCase,
    private val getNewPromotionUseCase: GetNewPromotionUseCase,
    private val userSession: UserSessionInterface,
    private val remoteConfig: FirebaseRemoteConfigImpl
) : BaseViewModel(dispatcher.main) {


    companion object {
        private const val DELAY_TIME = 5000L
        private const val GENTLY_SWIPE_DELAY = 1000L
        private const val START_TOPUP_ANIM_DELAY = 2000L
        private const val TOGGLE_TOPUP_ANIM_DELAY = 1000L

        private const val MAX_TOGGLE_TIMES = 4
        private const val ERROR_COUNT_THRESHOLD = 2

        private const val INVALID_FOLLOWERS_ERROR_MESSAGE = "Shop followers value is invalid"
    }

    private val _isToasterAlreadyShown = NonNullLiveData(false)
    private val _shopPeriodType = MutableLiveData<Result<ShopInfoPeriodUiModel>>()
    private val _shopShareInfoLiveData = MutableLiveData<OtherMenuShopShareData>()

    private val _freeShippingLiveData =
        MutableLiveData<SettingResponseState<TokoPlusBadgeUiModel>>()
    private val _shopBadgeLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _shopTotalFollowersLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _totalTokoMemberLiveData = MutableLiveData<SettingResponseState<String>>()

    private val _userShopInfoLiveData = MutableLiveData<SettingResponseState<ShopStatusUiModel>>()
    private val _shopOperationalLiveData =
        MutableLiveData<SettingResponseState<ShopOperationalData>>()
    private val _balanceInfoLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _kreditTopAdsFormattedLiveData = MutableLiveData<SettingResponseState<String>>()
    private val _isTopAdsAutoTopupLiveData = MutableLiveData<Result<Boolean>>()
    private val _isShowTagCentralizePromo = MutableLiveData<SettingResponseState<Boolean>>()

    val shopBadgeLiveData: LiveData<SettingResponseState<String>>
        get() = _shopBadgeLiveData
    val shopTotalFollowersLiveData: LiveData<SettingResponseState<String>>
        get() = _shopTotalFollowersLiveData
    val totalTokoMemberLiveData: LiveData<SettingResponseState<String>>
        get() = _totalTokoMemberLiveData
    val userShopInfoLiveData: LiveData<SettingResponseState<ShopStatusUiModel>>
        get() = _userShopInfoLiveData
    val shopOperationalLiveData: LiveData<SettingResponseState<ShopOperationalData>>
        get() = _shopOperationalLiveData
    val balanceInfoLiveData: LiveData<SettingResponseState<String>>
        get() = _balanceInfoLiveData
    val kreditTopAdsLiveData: LiveData<SettingResponseState<String>>
        get() = _kreditTopAdsFormattedLiveData
    val isTopAdsAutoTopupLiveData: LiveData<Result<Boolean>>
        get() = _isTopAdsAutoTopupLiveData
    val freeShippingLiveData: LiveData<SettingResponseState<TokoPlusBadgeUiModel>>
        get() = _freeShippingLiveData
    val isShowTagCentralizePromo: LiveData<SettingResponseState<Boolean>>
        get() = _isShowTagCentralizePromo

    private val _errorStateMap = MediatorLiveData<Map<OtherMenuDataType, Boolean>>().apply {
        addSource(_shopBadgeLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Badge, it)
        }
        addSource(_totalTokoMemberLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.TotalTokoMember, it)
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
        addSource(_kreditTopAdsFormattedLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.Topads, it)
        }
        addSource(_freeShippingLiveData) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.FreeShipping, it)
        }
        addSource(_isShowTagCentralizePromo) {
            value = value?.getUpdatedErrorMap(OtherMenuDataType.IsShowTagCentralizePromo, it)
        }
    }

    private val _secondarySuccessStateMap =
        MediatorLiveData<Map<OtherMenuDataType, Boolean>>().apply {
            addSource(_shopBadgeLiveData) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.Badge, it)
            }
            addSource(_totalTokoMemberLiveData) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.TotalTokoMember, it)
            }
            addSource(_shopTotalFollowersLiveData) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.Followers, it)
            }
            addSource(_userShopInfoLiveData) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.Status, it)
            }
            addSource(_shopOperationalLiveData) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.Operational, it)
            }
            addSource(_freeShippingLiveData) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.FreeShipping, it)
            }
            addSource(_isShowTagCentralizePromo) {
                value = value?.getUpdatedSuccessMap(OtherMenuDataType.IsShowTagCentralizePromo, it)
            }

        }

    private val _shouldShowMultipleErrorToaster = MediatorLiveData<Boolean>().apply {
        addSource(_errorStateMap) { map ->
            val errorCounts = map?.count { it.value }.orZero()
            if (errorCounts >= ERROR_COUNT_THRESHOLD) {
                if (_hasShownMultipleErrorToaster.value == false) {
                    value = true
                    _hasShownMultipleErrorToaster.value = true
                }
            } else {
                value = false
                _hasShownMultipleErrorToaster.value = false
            }
        }
    }
    val shouldShowMultipleErrorToaster: LiveData<Boolean>
        get() = _shouldShowMultipleErrorToaster
    private val _hasShownMultipleErrorToaster = MutableLiveData(false)

    val shouldSwipeSecondaryInfo: LiveData<Boolean>
        get() = _shouldSwipeSecondaryInfoGently
    private val _shouldSwipeSecondaryInfoGently = MediatorLiveData<Boolean>().apply {
        addSource(_secondarySuccessStateMap) { map ->
            val successCount = map?.count { it.value }.orZero()
            if (successCount == map?.count().orZero()) {
                launch(coroutineContext) {
                    withContext(dispatcher.main) {
                        swipeSecondaryInfoGentlyWithDelay()
                    }
                }
            } else {
                value = false
            }
        }
    }

    private val _kreditTopAdsLiveData = MutableLiveData<Float>()
    private val _numberOfTopupToggleCounts = MutableLiveData<Int>()
    val numberOfTopupToggleCounts: LiveData<Int>
        get() = _numberOfTopupToggleCounts

    val shopPeriodType: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopPeriodType
    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown
    val shopShareInfoLiveData: LiveData<OtherMenuShopShareData>
        get() = _shopShareInfoLiveData

    private var topadsTopupToggleJob: Job? = null

    fun getAllOtherMenuData() {
        setErrorStateMapDefaultValue()
        setSuccessStateMapDefaultValue()
        resetTopadsToggleCount()

        getShopBadgeData()
        getTotalTokomemberData()
        getShopTotalFollowersData()
        getUserShopInfoData()
        getFreeShippingStatusData()
        getShopOperationalData()
        getBalanceInfoData()
        getKreditTopAdsData()
        getIsTopAdsAutoTopup()
        getIsShowTagCentralizePromo()
        getShopPeriodType()
    }

    fun onShownMultipleError(isShown: Boolean = false) {
        _hasShownMultipleErrorToaster.value = isShown
    }

    fun onCheckDelayErrorResponseTrigger() {
        launch(coroutineContext) {
            _isToasterAlreadyShown.value.let { isToasterAlreadyShown ->
                if (!isToasterAlreadyShown) {
                    _isToasterAlreadyShown.value = true
                    delay(DELAY_TIME)
                    _isToasterAlreadyShown.value = false
                }
            }
        }
    }

    fun getShopPeriodType() {
        launchCatchError(block = {
            val periodData = withContext(dispatcher.io) {
                getShopCreatedInfoUseCase.requestParams =
                    GetShopCreatedInfoUseCase.createParams(userSession.shopId.toLongOrZero())
                getShopCreatedInfoUseCase.executeOnBackground()
            }
            _shopPeriodType.value = Success(periodData)
        }, onError = {
            _shopPeriodType.value = Fail(it)
        })
    }

    fun reloadErrorData() {
        _errorStateMap.value?.forEach {
            if (it.value) {
                when (it.key) {
                    OtherMenuDataType.Badge -> getShopBadge()
                    OtherMenuDataType.TotalTokoMember -> getTotalTokoMember()
                    OtherMenuDataType.Followers -> getShopTotalFollowers()
                    OtherMenuDataType.Status -> getUserShopInfo()
                    OtherMenuDataType.Operational -> getShopOperational()
                    OtherMenuDataType.Saldo -> getBalanceInfo()
                    OtherMenuDataType.FreeShipping -> getFreeShippingStatus()
                    OtherMenuDataType.IsShowTagCentralizePromo -> getIsShowTagCentralizePromo()
                    else -> getKreditTopAds()
                }
            }
        }
    }

    fun getFreeShippingStatus() {
        _freeShippingLiveData.value = SettingResponseState.SettingLoading
        getFreeShippingStatusData()
    }

    fun getShopBadge() {
        _shopBadgeLiveData.value = SettingResponseState.SettingLoading
        getShopBadgeData()
    }

    fun getTotalTokoMember() {
        _totalTokoMemberLiveData.value = SettingResponseState.SettingLoading
        getTotalTokomemberData()
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
        _kreditTopAdsFormattedLiveData.value = SettingResponseState.SettingLoading
        getKreditTopAdsData()
    }

    fun startToggleTopadsCredit() {
        if (topadsTopupToggleJob?.isCompleted != false) {
            topadsTopupToggleJob = launch {
                toggleTopadsTopupWithDelay()
            }
        }
    }

    fun getShopShareInfoData() {
        launchCatchError(
            block = {
                val shopShareInfo = withContext(dispatcher.io) {
                    shopShareInfoUseCase.execute(userSession.shopId)
                }
                _shopShareInfoLiveData.value = shopShareInfo
            },
            onError = {
                _shopShareInfoLiveData.value = null
            }
        )
    }

    fun setErrorStateMapDefaultValue() {
        if (_errorStateMap.value == null) {
            _errorStateMap.value = mutableMapOf(
                OtherMenuDataType.Badge to false,
                OtherMenuDataType.TotalTokoMember to false,
                OtherMenuDataType.Followers to false,
                OtherMenuDataType.Status to false,
                OtherMenuDataType.Operational to false,
                OtherMenuDataType.Saldo to false,
                OtherMenuDataType.Topads to false,
                OtherMenuDataType.FreeShipping to false,
                OtherMenuDataType.IsShowTagCentralizePromo to false

            )
        }
    }

    fun setSuccessStateMapDefaultValue() {
        if (_secondarySuccessStateMap.value == null) {
            _secondarySuccessStateMap.value = mutableMapOf(
                OtherMenuDataType.Badge to false,
                OtherMenuDataType.Followers to false,
                OtherMenuDataType.Status to false,
                OtherMenuDataType.Operational to false,
                OtherMenuDataType.FreeShipping to false,
                OtherMenuDataType.IsShowTagCentralizePromo to false

            )
        }
    }

    fun setDefaultToasterState(isShown: Boolean) {
        _isToasterAlreadyShown.value = isShown
    }

    fun getIsShowTagCentralizePromo() {
        launchCatchError(
            block = {
                val data = withContext(dispatcher.io) {
                    getNewPromotionUseCase.execute(userSession.shopId)
                }
                val isShow = data.data.pages.isNotEmpty()
                _isShowTagCentralizePromo.value = SettingResponseState.SettingSuccess(isShow)
            },
            onError = {
                _isShowTagCentralizePromo.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getFreeShippingStatusData() {
        val freeShippingDisabled =
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        val inTransitionPeriod =
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_TRANSITION_PERIOD, true)
        launchCatchError(block = {
            val freeShippingPair = withContext(dispatcher.io) {
                if (freeShippingDisabled || inTransitionPeriod) {
                    TokoPlusBadgeUiModel()
                } else {
                    getTokoPlusBadgeUseCase.execute(userSession.shopId)
                }
            }
            _freeShippingLiveData.value = SettingResponseState.SettingSuccess(freeShippingPair)
        }, onError = {
            _freeShippingLiveData.value = SettingResponseState.SettingError(it)
        })
    }

    private fun getShopBadgeData() {
        launchCatchError(
            block = {
                val badgeUrl = withContext(dispatcher.io) {
                    getShopBadgeUseCase.params =
                        GetShopBadgeUseCase.createRequestParams(userSession.shopId.toLongOrZero())
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
                    getShopTotalFollowersUseCase.params =
                        GetShopTotalFollowersUseCase.createRequestParams(userSession.shopId.toLongOrZero())
                    getShopTotalFollowersUseCase.executeOnBackground().let { shopFollowers ->
                        if (shopFollowers == Constant.INVALID_NUMBER_OF_FOLLOWERS) {
                            throw MessageErrorException(INVALID_FOLLOWERS_ERROR_MESSAGE)
                        } else {
                            shopFollowers
                        }
                    }
                }
                _shopTotalFollowersLiveData.value = SettingResponseState.SettingSuccess(
                    totalFollowers.thousandFormatted()
                )
            },
            onError = {
                _shopTotalFollowersLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getTotalTokomemberData() {
        launchCatchError(
            block = {
                val totalTokoMember = withContext(dispatcher.io) {
                    getShopTotalTokoMembersUseCase.params =
                        GetTotalTokoMemberUseCase.createRequestParams(userSession.shopId.toLongOrZero())
                    getShopTotalTokoMembersUseCase.executeOnBackground()
                }
                _totalTokoMemberLiveData.value = SettingResponseState.SettingSuccess(
                    totalTokoMember.thousandFormatted()
                )
            },
            onError = {
                _totalTokoMemberLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getUserShopInfoData() {
        launchCatchError(
            block = {
                val userShopInfoWrapper = withContext(dispatcher.io) {
                    getUserShopInfoUseCase.executeOnBackground()
                }
                _userShopInfoLiveData.value = SettingResponseState.SettingSuccess(
                    ShopStatusUiModel(
                        userShopInfoWrapper,
                        userSession
                    )
                )
                userShopInfoWrapper.shopType?.let {
                    updateShopInfoUserSession(it)
                }
            },
            onError = {
                _userShopInfoLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun updateShopInfoUserSession(shopType: ShopType) {
        when (shopType) {
            is ShopType.OfficialStore -> {
                userSession.setIsGoldMerchant(true)
                userSession.setIsPowerMerchantIdle(false)
                userSession.setIsShopOfficialStore(true)
            }
            // This means that the power merchant status is IDLE
            is PowerMerchantStatus.NotActive -> {
                userSession.setIsGoldMerchant(false)
                userSession.setIsPowerMerchantIdle(true)
                userSession.setIsShopOfficialStore(false)
            }
            is PowerMerchantStatus, is PowerMerchantProStatus -> {
                userSession.setIsGoldMerchant(true)
                userSession.setIsPowerMerchantIdle(false)
                userSession.setIsShopOfficialStore(false)
            }
            // This means that the status is Regular Merchant
            else -> {
                userSession.setIsGoldMerchant(false)
                userSession.setIsPowerMerchantIdle(false)
                userSession.setIsShopOfficialStore(false)
            }
        }
    }

    private fun getShopOperationalData() {
        launchCatchError(
            block = {
                val shopOperational = withContext(dispatcher.io) {
                    getShopOperationalUseCase.executeOnBackground()
                }
                _shopOperationalLiveData.value =
                    SettingResponseState.SettingSuccess(shopOperational)
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
                    balanceInfoUseCase.executeOnBackground().totalBalance
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
                val topAdsBalanceFormatted = withContext(dispatcher.io) {
                    topAdsDashboardDepositUseCase.params =
                        TopAdsDashboardDepositUseCase.createRequestParams(userSession.shopId)
                    val topAdsBalance = topAdsDashboardDepositUseCase.executeOnBackground()
                    _kreditTopAdsLiveData.postValue(topAdsBalance)
                    topAdsBalance.getCurrencyFormatted()
                }
                _kreditTopAdsFormattedLiveData.value =
                    SettingResponseState.SettingSuccess(topAdsBalanceFormatted)
            },
            onError = {
                _kreditTopAdsFormattedLiveData.value = SettingResponseState.SettingError(it)
            }
        )
    }

    private fun getIsTopAdsAutoTopup() {
        launchCatchError(
            block = {
                val isTopAdsAutoTopup = withContext(dispatcher.io) {
                    topAdsAutoTopupUseCase.params =
                        TopAdsAutoTopupUseCase.createRequestParams(userSession.shopId)
                    topAdsAutoTopupUseCase.executeOnBackground()
                }
                _isTopAdsAutoTopupLiveData.value = Success(isTopAdsAutoTopup)
            },
            onError = {
                _isTopAdsAutoTopupLiveData.value = Fail(it)
            }
        )
    }

    private suspend fun swipeSecondaryInfoGentlyWithDelay() {
        delay(GENTLY_SWIPE_DELAY)
        if (_shouldSwipeSecondaryInfoGently.value == false) {
            _shouldSwipeSecondaryInfoGently.postValue(true)
        }
    }

    private fun resetTopadsToggleCount() {
        _kreditTopAdsLiveData.value = null
        _numberOfTopupToggleCounts.value = null
        topadsTopupToggleJob?.cancel()
    }

    private suspend fun toggleTopadsTopupWithDelay() {
        withContext(dispatcher.main) {
            if (_kreditTopAdsLiveData.value == 0f) {
                val toggleCount: Int? = _numberOfTopupToggleCounts.value
                if (toggleCount == null) {
                    delay(START_TOPUP_ANIM_DELAY)
                    _numberOfTopupToggleCounts.postValue(1)
                } else if (toggleCount < MAX_TOGGLE_TIMES) {
                    delay(TOGGLE_TOPUP_ANIM_DELAY)
                    _numberOfTopupToggleCounts.postValue(toggleCount.inc())
                }
            }
        }
    }

    private fun SettingResponseState<*>.isError(): Boolean =
        this is SettingResponseState.SettingError

    private fun SettingResponseState<*>.isSuccess(): Boolean =
        this is SettingResponseState.SettingSuccess

    private fun Map<OtherMenuDataType, Boolean>.getUpdatedErrorMap(
        dataType: OtherMenuDataType,
        state: SettingResponseState<*>
    ): Map<OtherMenuDataType, Boolean> {
        return mapValues {
            if (it.key == dataType) {
                state.isError()
            } else {
                it.value
            }
        }
    }

    private fun Map<OtherMenuDataType, Boolean>.getUpdatedSuccessMap(
        dataType: OtherMenuDataType,
        state: SettingResponseState<*>
    ): Map<OtherMenuDataType, Boolean> {
        return mapValues {
            if (it.key == dataType) {
                state.isSuccess()
            } else {
                it.value
            }
        }
    }
}
