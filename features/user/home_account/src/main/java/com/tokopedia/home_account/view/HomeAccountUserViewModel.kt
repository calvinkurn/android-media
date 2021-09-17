package com.tokopedia.home_account.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.*
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
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
        private val getCentralizedUserAssetConfigUseCase: GetCentralizedUserAssetConfigUseCase,
        private val getBalanceAndPointUseCase: GetBalanceAndPointUseCase,
        private val getTokopointsBalanceAndPointUseCase: GetTokopointsBalanceAndPointUseCase,
        private val getSaldoBalanceUseCase: GetSaldoBalanceUseCase,
        private val getCoBrandCCBalanceAndPointUseCase: GetCoBrandCCBalanceAndPointUseCase,
        private val getWalletEligibleUseCase: GetWalletEligibleUseCase,
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

    private val _firstRecommendationData = MutableLiveData<Result<RecommendationWidget>>()
    val firstRecommendationData: LiveData<Result<RecommendationWidget>>
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

    private val _walletEligible = MutableLiveData<Result<WalletappWalletEligibility>>()
    val walletEligible: LiveData<Result<WalletappWalletEligibility>>
        get() = _walletEligible

    var internalBuyerData: UserAccountDataModel? = null

    fun setSafeMode(isActive: Boolean) {
        setUserProfileSafeModeUseCase.executeQuerySetSafeMode(
                { (userProfileSettingUpdate) ->
                    if (userProfileSettingUpdate.isSuccess) {
                        accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, isActive)
                        accountPref.saveSettingValue(AccountConstants.KEY.CLEAR_CACHE, isActive)
                    }
                },
                { throwable ->
                    throwable.printStackTrace()
                }, isActive)
    }

    fun getShortcutData() {
        launchCatchError(block = {
            val shortcutResponse = getUserShortcutUseCase.executeOnBackground()
            _shortcutData.value = Success(shortcutResponse)
        }, onError = {
            _shortcutData.postValue(Fail(it))
        })
    }

    fun getBuyerData() {
        launchCatchError(block = {
            val accountModel = getHomeAccountUserUseCase.executeOnBackground()
            withContext(dispatcher.main) {
                internalBuyerData = accountModel
                saveLocallyAttributes(accountModel)
                _buyerAccountData.value = Success(accountModel)
            }
        }, onError = {
            _buyerAccountData.postValue(Fail(it))
        })
    }

    fun getFirstRecommendation() {
        getRecommendation(page = 1)
    }

    fun getRecommendation(page: Int) {
        launchCatchError(block = {
            val recommendationWidget = getRecommendationList(page)
            if (checkFirstPage(page)) {
                _firstRecommendationData.postValue(Success(recommendationWidget))
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

    private fun getRecommendationList(page: Int): RecommendationWidget {
        val params = getRecommendationUseCase.getRecomParams(
                page,
                GetRecommendationUseCase.DEFAULT_VALUE_X_SOURCE,
                AKUN_PAGE,
                emptyList()
        )
        return getRecommendationUseCase.createObservable(params).toBlocking().first()[0]
    }

    fun getCentralizedUserAssetConfig(entryPoint: String) {
        launchCatchError(context = dispatcher.main, block = {
            val result = getCentralizedUserAssetConfigUseCase(entryPoint)
            _centralizedUserAssetConfig.value = Success(result.data)
        }, onError = {
            _centralizedUserAssetConfig.value = Fail(it)
        })
    }

    fun getBalanceAndPoint(walletId: String, assetConfig: AssetConfig? = null) {
        launchCatchError(context=dispatcher.main, block = {
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
                AccountConstants.WALLET.TOKOPOINT -> {
                    getTokopointsBalanceAndPointUseCase(Unit)
                }
                AccountConstants.WALLET.SALDO -> {
                    BalanceAndPointDataModel().apply {
                        assetConfig?.let {
                            this.data.id = assetConfig.id
                            this.data.title = assetConfig.title
                            this.data.subtitle = assetConfig.subtitle
                            this.data.icon = assetConfig.icon
                            this.data.applink = assetConfig.applink
                            this.data.weblink = assetConfig.weblink
                            this.data.isActive = assetConfig.isActive
                        }
                    }
                }
                AccountConstants.WALLET.CO_BRAND_CC -> {
                    getCoBrandCCBalanceAndPointUseCase(Unit)
                }
                else -> {
                    BalanceAndPointDataModel()
                }
            }

            if (result.data.id.isNotEmpty()) {
                _balanceAndPoint.value = ResultBalanceAndPoint.Success(result.data, walletId)
            } else {
                _balanceAndPoint.value = ResultBalanceAndPoint.Fail(IllegalArgumentException(), walletId)
            }
        }, onError = {
            _balanceAndPoint.value = ResultBalanceAndPoint.Fail(it, walletId)
        })
    }

    fun getGopayWalletEligible() {
        launchCatchError(context=dispatcher.main, block = {
            val params = getWalletEligibleUseCase.getParams(GOPAY_PARTNER_CODE, GOPAY_WALLET_CODE)
            val result = getWalletEligibleUseCase(params)

            _walletEligible.value = Success(result.data)
        }, onError = {
            _walletEligible.value = Fail(it)
        })
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

        private const val GOPAY_PARTNER_CODE = "PEMUDA"
        private const val GOPAYLATER_PARTNER_CODE = "PEMUDAPAYLATER"
        private const val OVO_PARTNER_CODE = "OVO"
        private const val GOPAY_WALLET_CODE = "PEMUDAPOINTS"
    }

}