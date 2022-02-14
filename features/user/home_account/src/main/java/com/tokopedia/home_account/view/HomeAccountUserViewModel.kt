package com.tokopedia.home_account.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.TDNBanner.TDN_INDEX
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.account_settings.domain.UserProfileSafeModeUseCase
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.home_account.linkaccount.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.linkaccount.domain.GetUserProfile
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class HomeAccountUserViewModel @Inject constructor(
    @Named(SessionModule.SESSION_MODULE)
    private val userSession: UserSessionInterface,
    private val accountPref: AccountPreference,
    private val getHomeAccountUserUseCase: HomeAccountUserUsecase,
    private val getUserShortcutUseCase: HomeAccountShortcutUseCase,
    private val setUserProfileSafeModeUseCase: SafeSettingProfileUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val getCentralizedUserAssetConfigUseCase: GetCentralizedUserAssetConfigUseCase,
    private val getBalanceAndPointUseCase: GetBalanceAndPointUseCase,
    private val getTokopointsBalanceAndPointUseCase: GetTokopointsBalanceAndPointUseCase,
    private val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
    private val getCoBrandCCBalanceAndPointUseCase: GetCoBrandCCBalanceAndPointUseCase,
    private val getLinkStatusUseCase: GetLinkStatusUseCase,
    private val getPhoneUseCase: GetUserProfile,
    private val userProfileSafeModeUseCase: UserProfileSafeModeUseCase,
    private val walletPref: WalletPref,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _buyerAccountData = MutableLiveData<Result<UserAccountDataModel>>()
    val buyerAccountDataData: LiveData<Result<UserAccountDataModel>>
        get() = _buyerAccountData

    private val _settingData = MutableLiveData<SettingDataView>()
    val settingData: LiveData<SettingDataView>
        get() = _settingData

    private val _settingApplication = MutableLiveData<SettingDataView>()
    val settingApplication: LiveData<SettingDataView>
        get() = _settingApplication

    private val _aboutTokopedia = MutableLiveData<SettingDataView>()
    val aboutTokopedia: LiveData<SettingDataView>
        get() = _aboutTokopedia

    private val _recommendationData = MutableLiveData<Result<List<RecommendationItem>>>()
    val getRecommendationData: LiveData<Result<List<RecommendationItem>>>
        get() = _recommendationData

    private val _firstRecommendationData = MutableLiveData<Result<RecommendationWidgetWithTDN>>()
    val firstRecommendationData: LiveData<Result<RecommendationWidgetWithTDN>>
        get() = _firstRecommendationData

    private val _shortcutData = MutableLiveData<Result<ShortcutResponse>>()
    val shortcutData: LiveData<Result<ShortcutResponse>>
        get() = _shortcutData

    private val _centralizedUserAssetConfig = MutableLiveData<Result<CentralizedUserAssetConfig>>()
    val centralizedUserAssetConfig: LiveData<Result<CentralizedUserAssetConfig>>
        get() = _centralizedUserAssetConfig

    private val _balanceAndPoint = MutableLiveData<ResultBalanceAndPoint<WalletappGetAccountBalance>>()
    val balanceAndPoint: LiveData<ResultBalanceAndPoint<WalletappGetAccountBalance>>
        get() = _balanceAndPoint

    private val _phoneNo = MutableLiveData<String>()
    val phoneNo: LiveData<String> get() = _phoneNo

    private val _safeModeStatus = MutableLiveData<Boolean>()
    val safeModeStatus: LiveData<Boolean> get() = _safeModeStatus

    var internalBuyerData: UserAccountDataModel? = null

    fun refreshPhoneNo() {
        launchCatchError(block = {
            val profile = getPhoneUseCase(Unit)
            val phone = profile.profileInfo.phone
            if (phone.isNotEmpty()) {
                userSession.phoneNumber = phone
                _phoneNo.value = phone
            }
        }, onError = {
            _phoneNo.value = ""
        })
    }

    fun getSafeModeValue() {
        userProfileSafeModeUseCase.executeQuerySafeMode(
            { (profileSettingResponse) ->
                accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, profileSettingResponse.safeMode)
                _safeModeStatus.value = profileSettingResponse.safeMode
            },
            { it.printStackTrace() }
        )
    }

    fun setSafeMode(isActive: Boolean) {
        setUserProfileSafeModeUseCase.executeQuerySetSafeMode(
                { getSafeModeValue() },
                { throwable ->
                    throwable.printStackTrace()
                }, isActive)
    }

    fun getShortcutData() {
        launchCatchError(block = {
            val shortcutResponse = getUserShortcutUseCase(Unit)
            _shortcutData.value = Success(shortcutResponse)
        }, onError = {
            _shortcutData.value = Fail(it)
        })
    }

    private suspend fun getLinkStatus(): LinkStatusResponse {
        return getLinkStatusUseCase(GetLinkStatusUseCase.ACCOUNT_LINKING_TYPE)
    }

    fun getBuyerData() {
        launchCatchError(block = {
            val accountModel = getHomeAccountUserUseCase(Unit)
            val linkStatus = getLinkStatus()
            accountModel.linkStatus = linkStatus.response
            internalBuyerData = accountModel
            saveLocallyAttributes(accountModel)
            _buyerAccountData.value = Success(accountModel)
        }, onError = {
            _buyerAccountData.value = Fail(it)
        })
    }

    fun getFirstRecommendation() {
        getRecommendation(page = 1)
    }

    fun getRecommendation(page: Int) {
        launchCatchError(block = {
            val recommendationWidget = getRecommendationList(page)
            var tdnBanner: TopAdsImageViewModel? = null
            if (recommendationWidget.recommendationItemList.size >= TDN_INDEX && checkFirstPage(page)) {
                tdnBanner = getTdnBannerData()
            }
            val data = RecommendationWidgetWithTDN(recommendationWidget, tdnBanner)
            if (checkFirstPage(page)) {
                _firstRecommendationData.postValue(Success(data))
            } else {
                _recommendationData.postValue(Success(recommendationWidget.recommendationItemList))
            }
        }, onError = {
            if (checkFirstPage(page)) {
                _firstRecommendationData.postValue(Fail(it))
            } else {
                _recommendationData.postValue(Fail(it))
            }
        })

    }

    private suspend fun getTdnBannerData(): TopAdsImageViewModel? {
        return try {
            val queryParams =
                topAdsImageViewUseCase.getQueryMap(
                    AccountConstants.TDNBanner.EMPTY,
                    AccountConstants.TDNBanner.SOURCE,
                    AccountConstants.TDNBanner.EMPTY,
                    AccountConstants.TDNBanner.ADS_COUNT,
                    AccountConstants.TDNBanner.DIMEN_ID,
                    AccountConstants.TDNBanner.EMPTY
                )
            topAdsImageViewUseCase.getImageData(queryParams).firstOrNull()
        } catch (t: Throwable) {
            null
        }
    }

    private suspend fun getRecommendationList(page: Int): RecommendationWidget {
        val recommendationParams = GetRecommendationRequestParam(
            pageNumber = page,
            xSource = DEFAULT_VALUE_X_SOURCE,
            pageName = AKUN_PAGE,
            productIds = emptyList(),
            xDevice = DEFAULT_VALUE_X_DEVICE
        )
        return getRecommendationUseCase.getData(recommendationParams).first()
    }

    fun getCentralizedUserAssetConfig(entryPoint: String) {
        launchCatchError(block = {
            val result = getCentralizedUserAssetConfigUseCase(entryPoint)
            _centralizedUserAssetConfig.value = Success(result.data)
        }, onError = {
            _centralizedUserAssetConfig.value = Fail(it)
        })
    }

    fun getBalanceAndPoint(walletId: String) {
        launchCatchError(block = {
            when (walletId) {
                AccountConstants.WALLET.TOKOPOINT -> {
                    val result = getTokopointsBalanceAndPointUseCase(Unit)
                    setBalanceAndPointValue(result.data, walletId)
                }
                AccountConstants.WALLET.SALDO -> {
                    val result = getSaldoBalanceUseCase(Unit)
                    setBalanceAndPointValue(result.data, walletId)
                }
                AccountConstants.WALLET.CO_BRAND_CC -> {
                    val result = getCoBrandCCBalanceAndPointUseCase(Unit)
                    setBalanceAndPointValue(result.data, walletId)
                }
                else -> {
                    getOtherBalanceAndPoint(walletId)
                }
            }
        }, onError = {
            _balanceAndPoint.value = ResultBalanceAndPoint.Fail(it, walletId)
        })
    }

    private suspend fun getOtherBalanceAndPoint(walletId: String) {
        val result = when (walletId) {
            AccountConstants.WALLET.GOPAY -> {
                getBalanceAndPointUseCase(GOPAY_PARTNER_CODE)
            }
            AccountConstants.WALLET.GOPAYLATER -> {
                getBalanceAndPointUseCase(GOPAYLATER_PARTNER_CODE)
            }
            AccountConstants.WALLET.OVO -> {
                getBalanceAndPointUseCase(OVO_PARTNER_CODE)
            }
            else -> {
                BalanceAndPointDataModel()
            }
        }
        setBalanceAndPointValue(result.data, walletId)
    }

    private fun setBalanceAndPointValue(data: WalletappGetAccountBalance, walletId: String) {
        if (data.id.isNotEmpty()) {
            _balanceAndPoint.value = ResultBalanceAndPoint.Success(data, walletId)
        } else {
            _balanceAndPoint.value = ResultBalanceAndPoint.Fail(IllegalArgumentException(), walletId)
        }
    }

    private fun checkFirstPage(page: Int): Boolean = page == 1

    fun saveLocallyAttributes(accountDataModel: UserAccountDataModel) {
        savePhoneVerified(accountDataModel)
        saveIsAffiliateStatus(accountDataModel)
        saveDebitInstantData(accountDataModel)
    }

    private fun saveDebitInstantData(accountDataModel: UserAccountDataModel) {
        accountDataModel.debitInstant.data?.let {
            walletPref.saveDebitInstantUrl(it.redirectUrl)
        }
    }

    private fun savePhoneVerified(accountDataModel: UserAccountDataModel) {
        accountDataModel.profile.let {
            userSession.setIsMSISDNVerified(it.isPhoneVerified)
        }
    }

    private fun saveIsAffiliateStatus(accountDataModel: UserAccountDataModel) {
        userSession.setIsAffiliateStatus(accountDataModel.isAffiliate)
    }

    companion object {
        private const val AKUN_PAGE = "account"

        private const val DEFAULT_VALUE_X_SOURCE = "recom_widget"
        private const val DEFAULT_VALUE_X_DEVICE = "android"

        private const val GOPAY_PARTNER_CODE = "PEMUDA"
        private const val GOPAYLATER_PARTNER_CODE = "PEMUDAPAYLATER"
        private const val OVO_PARTNER_CODE = "OVO"
        private const val GOPAY_WALLET_CODE = "PEMUDAPOINTS"
    }

}