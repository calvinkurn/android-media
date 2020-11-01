package com.tokopedia.home_account.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.MemberDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.domain.usecase.HomeAccountShortcutUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase
import com.tokopedia.home_account.domain.usecase.HomeAccountWalletBalanceUseCase
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
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
        private val walletPref: WalletPref,
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private val _buyerAccountData = MutableLiveData<Result<UserAccountDataModel>>()
    val buyerAccountDataData: LiveData<Result<UserAccountDataModel>>
        get() = _buyerAccountData

    private val _profileLiveData = MutableLiveData<ProfileDataView>()
    val profileData: LiveData<ProfileDataView>
        get() = _profileLiveData

    private val _settingData = MutableLiveData<SettingDataView>()
    val settingData: LiveData<SettingDataView>
        get() = _settingData

    private val _settingApplication = MutableLiveData<SettingDataView>()
    val settingApplication: LiveData<SettingDataView>
        get() = _settingApplication

    private val _aboutTokopedia = MutableLiveData<SettingDataView>()
    val aboutTokopedia: LiveData<SettingDataView>
        get() = _aboutTokopedia

    private val _memberData = MutableLiveData<MemberDataView>()
    val memberData: LiveData<MemberDataView>
        get() = _memberData

    fun setSafeMode(isActive: Boolean){
//        val savedValue: Boolean = !accountPref.getSafeMode()
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

    fun getBuyerData() {
        launchCatchError(block = {
            val accountModel = getHomeAccountUserUseCase.executeOnBackground()
            val walletModel = getBuyerWalletBalance()
//            val isAffiliate = checkIsAffiliate()
            val shortcutResponse = getUserShortcutUseCase.executeOnBackground()
            withContext(dispatcher) {
                accountModel.wallet = walletModel
//                accountModel.isAffiliate = isAffiliate
                accountModel.shortcutResponse = shortcutResponse
                saveLocallyAttributes(accountModel)
                _buyerAccountData.value = Success(accountModel)
//                _profileLiveData.value =
            }
        }, onError = {
            _buyerAccountData.postValue(Fail(it))
        })
    }

    private fun getBuyerWalletBalance(): WalletModel {
        return getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
    }

//    private fun getSettingData() {
//        _settingData.value = menuGenerator.generateUserSettingMenu()
//    }
//
//    private fun getSettingApp(){
//        _settingApplication.value = menuGenerator.generateApplicationSettingMenu(accountPref, permissionChecker)
//    }
//
//
//    private fun getAboutTokopediaData() {
//        _aboutTokopedia.value = menuGenerator.generateAboutTokopediaSettingMenu()
//    }

    fun getInitialData() {
//        getSettingData()
//        getSettingApp()
//        getAboutTokopediaData()
    }

    private fun saveLocallyWallet(accountDataModel: UserAccountDataModel) {
        walletPref.saveWallet(accountDataModel.wallet)
        if (accountDataModel.vccUserStatus != null) {
            walletPref.tokoSwipeUrl = accountDataModel.vccUserStatus.redirectionUrl
        }
    }

    private fun saveLocallyVccUserStatus(accountDataModel: UserAccountDataModel) {
        if (accountDataModel.vccUserStatus != null) {
            walletPref.saveVccUserStatus(accountDataModel.vccUserStatus)
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
        if (accountDataModel.debitInstant != null && accountDataModel.debitInstant.data != null) {
            walletPref.saveDebitInstantUrl(accountDataModel.debitInstant.data.redirectUrl)
        }
    }

    private fun savePhoneVerified(accountDataModel: UserAccountDataModel) {
        if (accountDataModel.profile != null) {
            userSession.setIsMSISDNVerified(accountDataModel.profile.isPhoneVerified)
        }
    }

    private fun saveIsAffiliateStatus(accountDataModel: UserAccountDataModel) {
        userSession.setIsAffiliateStatus(accountDataModel.isAffiliate)
    }

}