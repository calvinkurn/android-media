package com.tokopedia.home_account.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.domain.usecase.HomeAccountShortcutUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase
import com.tokopedia.home_account.domain.usecase.HomeAccountWalletBalanceUseCase
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class HomeAccountUserViewModel @Inject constructor(
        @Named(SessionModule.SESSION_MODULE)
        private val userSession: UserSessionInterface,
        private val accountPref: AccountPreference,
        private val getHomeAccountUserUseCase: HomeAccountUserUsecase,
        private val getUserShortcutUseCase: HomeAccountShortcutUseCase,
        private val getBuyerWalletBalanceUseCase: HomeAccountWalletBalanceUseCase,
        private val setUserProfileSafeModeUseCase: SafeSettingProfileUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val walletPref: WalletPref,
        private val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

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
    val firstRecommendationData : LiveData<Result<RecommendationWidget>>
        get() = _firstRecommendationData

    private val _walletData = MutableLiveData<Result<WalletModel>>()
    val walletData : LiveData<Result<WalletModel>>
        get() = _walletData

    private val _shortcutData = MutableLiveData<Result<ShortcutResponse>>()
    val shortcutData : LiveData<Result<ShortcutResponse>>
        get() = _shortcutData

    var internalBuyerData: UserAccountDataModel? = null

    fun setSafeMode(isActive: Boolean){
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

    fun getShortcutData(){
        launchCatchError(block = {
            val shortcutResponse = getUserShortcutUseCase.executeOnBackground()
            _shortcutData.value = Success(shortcutResponse)
        }, onError = {
            _shortcutData.postValue(Fail(it))
        })
    }

    fun getWalletBalance(){
        launchCatchError(block = {
            val wallet = getBuyerWalletBalance()
                _walletData.value = Success(wallet)
        }, onError = {
            _walletData.postValue(Fail(it))
        })
    }

    fun getBuyerData() {
        launchCatchError(block = {
            val accountModel = getHomeAccountUserUseCase.executeOnBackground()
            withContext(dispatcher) {
                internalBuyerData = accountModel
                saveLocallyAttributes(accountModel)
                _buyerAccountData.value = Success(accountModel)
            }
        }, onError = {
            _buyerAccountData.postValue(Fail(it))
        })
    }

    private fun getBuyerWalletBalance(): WalletModel {
        return getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
    }

    fun getFirstRecommendation() {
        getRecommendation(page = 1)
    }

    fun getRecommendation(page: Int) {
        launchCatchError(Dispatchers.IO, block = {
            val recommendationWidget = getRecommendationList(page)
            if(checkFirstPage(page)) {
                _firstRecommendationData.postValue(Success(recommendationWidget))
            } else {
                _recommendationData.postValue(Success(recommendationWidget.recommendationItemList))
            }
        }, onError = {
            if(checkFirstPage(page)) {
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
        return getRecommendationUseCase.createObservable(params).toBlocking().single()[0]
    }

    private fun checkFirstPage(page: Int): Boolean = page == 1

    private fun saveLocallyWallet(accountDataModel: UserAccountDataModel) {
        walletPref.saveWallet(accountDataModel.wallet)
        accountDataModel?.vccUserStatus?.let{
            walletPref.tokoSwipeUrl = it.redirectionUrl
        }
    }

    private fun saveLocallyVccUserStatus(accountDataModel: UserAccountDataModel) {
        accountDataModel?.vccUserStatus?.let{
            walletPref.saveVccUserStatus(it)
        }
    }

    fun saveLocallyAttributes(accountDataModel: UserAccountDataModel) {
        saveLocallyWallet(accountDataModel)
        saveLocallyVccUserStatus(accountDataModel)
        savePhoneVerified(accountDataModel)
        saveIsAffiliateStatus(accountDataModel)
        saveDebitInstantData(accountDataModel)
    }

    private fun saveDebitInstantData(accountDataModel: UserAccountDataModel) {
        accountDataModel?.debitInstant?.data?.let {
            walletPref.saveDebitInstantUrl(it.redirectUrl)
        }
    }

    private fun savePhoneVerified(accountDataModel: UserAccountDataModel) {
        accountDataModel?.profile?.let {
            userSession.setIsMSISDNVerified(it.isPhoneVerified)
        }
    }

    private fun saveIsAffiliateStatus(accountDataModel: UserAccountDataModel) {
        userSession.setIsAffiliateStatus(accountDataModel.isAffiliate)
    }

    companion object {
        private const val AKUN_PAGE = "account"
    }

}