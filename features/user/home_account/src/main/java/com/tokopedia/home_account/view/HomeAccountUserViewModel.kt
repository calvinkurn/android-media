package com.tokopedia.home_account.view

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.TDNBanner.TDN_INDEX
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetConfig
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.RecommendationWidgetWithTDN
import com.tokopedia.home_account.data.model.SafeModeParam
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.data.pref.AccountPreference
import com.tokopedia.home_account.domain.usecase.GetBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.GetCentralizedUserAssetConfigUseCase
import com.tokopedia.home_account.domain.usecase.GetCoBrandCCBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.GetSafeModeUseCase
import com.tokopedia.home_account.domain.usecase.GetSaldoBalanceUseCase
import com.tokopedia.home_account.domain.usecase.GetTokopointsBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountShortcutUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase
import com.tokopedia.home_account.domain.usecase.OfferInterruptUseCase
import com.tokopedia.home_account.domain.usecase.SaveAttributeOnLocalUseCase
import com.tokopedia.home_account.domain.usecase.UpdateSafeModeUseCase
import com.tokopedia.home_account.view.mapper.ProfileWithDataStoreMapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintResult
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreference
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.sessioncommon.domain.usecase.GetOclStatusUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.domain.usecase.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusCons
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeAccountUserViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val accountPref: AccountPreference,
    private val fingerprintPreference: FingerprintPreference,
    private val getHomeAccountUserUseCase: HomeAccountUserUsecase,
    private val getUserShortcutUseCase: HomeAccountShortcutUseCase,
    private val setUserProfileSafeModeUseCase: UpdateSafeModeUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val getCentralizedUserAssetConfigUseCase: GetCentralizedUserAssetConfigUseCase,
    private val getBalanceAndPointUseCase: GetBalanceAndPointUseCase,
    private val getTokopointsBalanceAndPointUseCase: GetTokopointsBalanceAndPointUseCase,
    private val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
    private val getCoBrandCCBalanceAndPointUseCase: GetCoBrandCCBalanceAndPointUseCase,
    private val userProfileSafeModeUseCase: GetSafeModeUseCase,
    private val checkFingerprintToggleStatusUseCase: CheckFingerprintToggleStatusUseCase,
    private val tokopediaPlusUseCase: TokopediaPlusUseCase,
    private val saveAttributeOnLocal: SaveAttributeOnLocalUseCase,
    private val offerInterruptUseCase: OfferInterruptUseCase,
    private val userProfileAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    private val profileWithDataStoreMapper: ProfileWithDataStoreMapper,
    private val getOclStatusUseCase: GetOclStatusUseCase,
    private val oclPreference: OclPreference,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _buyerAccountData = MutableLiveData<Result<ProfileDataView>>()
    val buyerAccountDataData: LiveData<Result<ProfileDataView>>
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

    private val _safeModeStatus = MutableLiveData<Boolean>()
    val safeModeStatus: LiveData<Boolean> get() = _safeModeStatus

    private val mutableCheckFingerprintStatus = MutableLiveData<Result<CheckFingerprintResult>>()
    val checkFingerprintStatus: LiveData<Result<CheckFingerprintResult>>
        get() = mutableCheckFingerprintStatus

    private val _tokopediaPlusData = MutableLiveData<Result<TokopediaPlusDataModel>>()
    val tokopediaPlusData: LiveData<Result<TokopediaPlusDataModel>>
        get() = _tokopediaPlusData

    private val _isOclEligible = MutableLiveData<Boolean>()
    val isOclEligible: LiveData<Boolean>
        get() = _isOclEligible

    private val _refreshAndUpdateLayoutProfile = MutableLiveData<ProfileDataView>()
    val refreshAndUpdateLayoutProfile: LiveData<ProfileDataView> get() = _refreshAndUpdateLayoutProfile

    @SuppressLint("PII Data Exposure")
    fun refreshUserProfile(isUpdateLayout : Boolean = false) {
        launch {
            try {
                val result = userProfileAndSaveSessionUseCase(Unit)

                if (isUpdateLayout) {
                    val buyerAccountData = _buyerAccountData.value
                    if (buyerAccountData is Success && result is Success) {
                        val oldProfile = buyerAccountData.data
                        val newProfile = result.data.profileInfo
                        val newProfileData = ProfileDataView(
                            name = newProfile.fullName,
                            phone = newProfile.phone,
                            email = newProfile.email,
                            avatar = newProfile.profilePicture,
                            isLinked = oldProfile.isLinked,
                            isShowLinkStatus = oldProfile.isShowLinkStatus,
                            memberStatus = oldProfile.memberStatus,
                            isSuccessGetTokopediaPlusData = oldProfile.isSuccessGetTokopediaPlusData,
                            tokopediaPlusWidget = oldProfile.tokopediaPlusWidget,
                            offerInterruptData = oldProfile.offerInterruptData
                        )
                        _refreshAndUpdateLayoutProfile.value = newProfileData
                    }
                }
            } catch (ignored: Exception) {}
        }
    }

    fun getFingerprintStatus() {
        launchCatchError(block = {
            val result = checkFingerprintToggleStatusUseCase(userSession.userId).data
            if (result.isSuccess && result.errorMessage.isEmpty()) {
                mutableCheckFingerprintStatus.postValue(Success(result))
            } else {
                mutableCheckFingerprintStatus.value = Fail(MessageErrorException(result.errorMessage))
            }
        }, onError = {
                mutableCheckFingerprintStatus.value = Fail(it)
            })
    }

    fun getOclStatus() {
        launch {
            try {
                val result = getOclStatusUseCase(oclPreference.getToken())
                _isOclEligible.value = result.isShowing
            } catch (ignored: Exception) { }
        }
    }

    fun setOneTapStatus(isEligible: Boolean) {
        _isOclEligible.value = isEligible
    }

    fun setSafeMode(isActive: Boolean) {
        launch {
            try {
                val param = SafeModeParam(isActive)
                setUserProfileSafeModeUseCase(param)
                val safeMode = userProfileSafeModeUseCase(Unit).userProfileSetting.safeMode
                updateSaveModeValue(safeMode)
            } catch (ignored: Exception) { }
        }
    }

    fun getSafeModeValue() {
        launch {
            try {
                val result = userProfileSafeModeUseCase(Unit)
                updateSaveModeValue(result.userProfileSetting.safeMode)
            } catch (ignored: Exception) { }
        }
    }

    fun updateSaveModeValue(isActive: Boolean) {
        accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
        _safeModeStatus.value = isActive
    }

    fun getShortcutData() {
        launch {
            try {
                val shortcutResponse = getUserShortcutUseCase(Unit)
                _shortcutData.value = Success(shortcutResponse)
            } catch (e: Exception) {
                _shortcutData.value = Fail(e)
            }
        }
    }

    fun getBuyerData(isSupportBiometric: Boolean = false) {
        launch {
            try {
                coroutineScope {
                    val homeAccountUser = async { getHomeAccountUserUseCase(Unit) }
                    val offerInterruption = offerInterruptUseCase(
                        mapOf(
                            OfferInterruptUseCase.PARAM_SUPPORT_BIOMETRIC to isSupportBiometric,
                            OfferInterruptUseCase.PARAM_DEVICE_BIOMETRIC to fingerprintPreference.getUniqueId()
                        )
                    )

                    val accountModel = homeAccountUser.await().apply {
                        this.offerInterrupt = offerInterruption.data
                    }

                    _buyerAccountData.value = Success(profileWithDataStoreMapper(accountModel))
                    // This is executed after setting live data to save load time
                    saveAttributeOnLocal(accountModel)
                }
            } catch (e: Exception) {
                _buyerAccountData.value = Fail(e)
            }
        }
    }

    fun getFirstRecommendation() {
        getRecommendation(page = 1)
    }

    fun getRecommendation(page: Int) {
        launchCatchError(block = {
            val recommendationWidget = getRecommendationList(page)
            var tdnBanner: TopAdsImageUiModel? = null
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

    private suspend fun getTdnBannerData(): TopAdsImageUiModel? {
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

    fun getBalanceAndPoint(walletId: String, hideTitleText: Boolean, titleText: String) {
        launchCatchError(block = {
            when (walletId) {
                AccountConstants.WALLET.TOKOPOINT -> {
                    val result = getTokopointsBalanceAndPointUseCase(Unit)
                    result.data.apply {
                        titleAsset = titleText
                        hideTitle = hideTitleText
                    }
                    setBalanceAndPointValue(result.data, walletId)
                }
                AccountConstants.WALLET.SALDO -> {
                    val result = getSaldoBalanceUseCase(Unit)
                    result.data.apply {
                        titleAsset = titleText
                        hideTitle = hideTitleText
                    }
                    setBalanceAndPointValue(result.data, walletId)
                }
                AccountConstants.WALLET.CO_BRAND_CC -> {
                    val result = getCoBrandCCBalanceAndPointUseCase(Unit)
                    result.data.apply {
                        titleAsset = titleText
                        hideTitle = hideTitleText
                    }
                    setBalanceAndPointValue(result.data, walletId)
                }
                else -> {
                    getOtherBalanceAndPoint(walletId, hideTitleText)
                }
            }
        }, onError = {
                _balanceAndPoint.value = ResultBalanceAndPoint.Fail(it, walletId)
            })
    }

    /**
     * same API
     */
    private suspend fun getOtherBalanceAndPoint(walletId: String, hideTitleText: Boolean) {
        val result = when (walletId) {
            AccountConstants.WALLET.GOPAY -> {
                getBalanceAndPointUseCase(GOPAY_PARTNER_CODE)
            }
            AccountConstants.WALLET.GOPAYLATER -> {
                getBalanceAndPointUseCase(GOPAYLATER_PARTNER_CODE)
            }
            AccountConstants.WALLET.GOPAYLATERCICIL -> {
                getBalanceAndPointUseCase(GOPAYLATERCICIL_PARTNER_CODE)
            }
            AccountConstants.WALLET.OVO -> {
                getBalanceAndPointUseCase(OVO_PARTNER_CODE)
            }
            else -> {
                BalanceAndPointDataModel()
            }
        }
        result.data.apply {
            hideTitle = hideTitleText
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

    /**
     * Tokopedia Plus (Goto Plus)
     */
    fun getTokopediaWidgetContent() {
        launchCatchError(coroutineContext, {
            val response = tokopediaPlusUseCase(
                mapOf(
                    TokopediaPlusUseCase.PARAM_SOURCE to TokopediaPlusCons.SOURCE_ACCOUNT_PAGE
                )
            )

            _tokopediaPlusData.value = Success(response.tokopediaPlus)
        }, {
            _tokopediaPlusData.value = Fail(it)
        })
    }

    private fun checkFirstPage(page: Int): Boolean = page == 1

    companion object {
        private const val AKUN_PAGE = "account"

        private const val DEFAULT_VALUE_X_SOURCE = "recom_widget"
        private const val DEFAULT_VALUE_X_DEVICE = "android"

        private const val GOPAY_PARTNER_CODE = "PEMUDA"
        private const val GOPAYLATER_PARTNER_CODE = "PEMUDAPAYLATER"
        private const val GOPAYLATERCICIL_PARTNER_CODE = "PEMUDACICIL"
        private const val OVO_PARTNER_CODE = "OVO"
        private const val GOPAY_WALLET_CODE = "PEMUDAPOINTS"
    }
}
