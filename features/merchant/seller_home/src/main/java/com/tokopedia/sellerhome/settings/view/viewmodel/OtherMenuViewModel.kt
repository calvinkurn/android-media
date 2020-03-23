package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.domain.toDecimalRupiahCurrency
import com.tokopedia.sellerhome.settings.domain.usecase.*
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named
class OtherMenuViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val getSettingShopInfoUseCase: GetSettingShopInfoUseCase,
        private val shopStatusTypeUseCase: ShopStatusTypeUseCase,
        private val topAdsDashboardDepositUseCase: TopAdsDashboardDepositUseCase,
        private val topAdsAutoTopupUseCase: TopAdsAutoTopupUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase
): BaseViewModel(dispatcher) {

    companion object {
        private const val DELAY_TIME = 5000L
    }

    private val _settingShopInfoLiveData = MutableLiveData<Result<SettingShopInfoUiModel>>()
    private val _isToasterAlreadyShown = MutableLiveData<Boolean>().apply { value = false }
    private val _isStatusBarInitialState = MutableLiveData<Boolean>().apply { value = true }

    val settingShopInfoLiveData: LiveData<Result<SettingShopInfoUiModel>>
        get() = _settingShopInfoLiveData
    val isStatusBarInitialState: LiveData<Boolean>
        get() = _isStatusBarInitialState

    fun getAllSettingShopInfo(isRetry: Boolean = false) {
        _isToasterAlreadyShown.value = isRetry
        getAllShopInfoData()
    }

    fun setIsStatusBarInitialState(isInitialState: Boolean) {
        _isStatusBarInitialState.value = isInitialState
    }

    private fun getAllShopInfoData() {
        val userId = userSession.userId
        val shopId = userSession.shopId
        launchCatchError(block = {
            val shopInfo = getSuspendSettingShopInfo(userId.toIntOrZero())
            val shopStatus = getSuspendShopType(shopId.toIntOrZero())
            val topAdsDeposit = getSuspendTopAdsDeposit(shopId.toIntOrZero())
            val isTopAdsAutoTopup = getSuspendTopAdsAutoTopup(shopId)
            val totalFollowers = getSuspendShopTotalFollowers(shopId.toIntOrZero())
            val shopBadge = getSuspendShopBadge(shopId.toIntOrZero())
            _settingShopInfoLiveData.value = Success(mapToSettingShopInfo(shopInfo, shopStatus, topAdsDeposit, isTopAdsAutoTopup, totalFollowers, shopBadge))
        }, onError = {
            _settingShopInfoLiveData.value = Fail(it)
        })
    }

    private fun mapToSettingShopInfo(shopInfo: ShopInfo,
                                     shopStatusType: ShopType,
                                     topAdsBalance: Float,
                                     isTopAdsAutoTopup: Boolean,
                                     totalFollowers: Int,
                                     shopBadge: String): SettingShopInfoUiModel {
        shopInfo.shopInfoMoengage?.run {
            return SettingShopInfoUiModel(
                    info?.shopName.toEmptyStringIfNull(),
                    info?.shopAvatar.toEmptyStringIfNull(),
                    shopStatusType,
                    shopInfo.balance?.sellerBalance ?: "",
                    topAdsBalance.toDecimalRupiahCurrency(),
                    isTopAdsAutoTopup,
                    shopBadge,
                    totalFollowers)
        }
        return SettingShopInfoUiModel()
    }

    private suspend fun getSuspendSettingShopInfo(userId: Int): ShopInfo {
        getSettingShopInfoUseCase.params = GetSettingShopInfoUseCase.createRequestParams(userId)
        return getSettingShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopType(shopId: Int): ShopType {
        shopStatusTypeUseCase.params = ShopStatusTypeUseCase.createRequestParams(shopId)
        return shopStatusTypeUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopTotalFollowers(shopId: Int): Int {
        getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
        return getShopTotalFollowersUseCase.executeOnBackground()
    }

    private suspend fun getSuspendShopBadge(shopId: Int): String {
        getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
        return getShopBadgeUseCase.executeOnBackground()
    }

    private suspend fun getSuspendTopAdsDeposit(shopId: Int): Float {
        topAdsDashboardDepositUseCase.params = TopAdsDashboardDepositUseCase.createRequestParams(shopId)
        return topAdsDashboardDepositUseCase.executeOnBackground()
    }

    private suspend fun getSuspendTopAdsAutoTopup(shopId: String): Boolean {
        topAdsAutoTopupUseCase.params = TopAdsAutoTopupUseCase.createRequestParams(shopId)
        return topAdsAutoTopupUseCase.executeOnBackground()
    }

    private suspend fun checkDelayErrorResponseTrigger(action: () -> Unit) {
        _isToasterAlreadyShown.value?.let { isToasterAlreadyShown ->
            if (!isToasterAlreadyShown){
                _isToasterAlreadyShown.value = true
                action.invoke()
                delay(DELAY_TIME)
                _isToasterAlreadyShown.value = false
            }
        }
    }

}

