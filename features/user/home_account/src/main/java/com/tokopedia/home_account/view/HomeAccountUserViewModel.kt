package com.tokopedia.home_account.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.domain.usecase.HomeAccountShortcutUseCase
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase
import com.tokopedia.home_account.domain.usecase.HomeAccountWalletBalanceUseCase
import com.tokopedia.home_account.domain.usecase.SafeSettingProfileUseCase
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeAccountUserViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val accountPref: AccountPreference,
        private val getHomeAccountUserUseCase: HomeAccountUserUsecase,
        private val getUserShortcutUseCase: HomeAccountShortcutUseCase,
        private val getBuyerWalletBalanceUseCase: HomeAccountWalletBalanceUseCase,
        private val setUserProfileSafeModeUseCase: SafeSettingProfileUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val walletPref: WalletPref,
        private val permissionChecker: PermissionChecker,
        private val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

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

    private val _recommendationData = MutableLiveData<Result<List<RecommendationItem>>>()
    val getRecommendationData: LiveData<Result<List<RecommendationItem>>>
        get() = _recommendationData

    private val _firstRecommendationData = MutableLiveData<Result<RecommendationWidget>>()
    val firstRecommendationData : LiveData<Result<RecommendationWidget>>
        get() = _firstRecommendationData

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

    private fun getSettingData() {
        _settingData.value =
                SettingDataView("Pengaturan Akun", mutableListOf(
                        CommonDataView(applink = ApplinkConstInternalLogistic.MANAGE_ADDRESS, title = "Daftar Alamat", body = "Atur alamat pengiriman belanjaan", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop, id = AccountConstants.SettingCode.SETTING_ACCOUNT_ADDRESS_ID),
                        CommonDataView(applink = ApplinkConstInternalGlobal.SETTING_BANK, title = "Rekening Bank", body = "Tarik Saldo Tokopedia ke rekening tujuan", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_bank, id = AccountConstants.SettingCode.SETTING_BANK_ACCOUNT_ID),
                        CommonDataView(applink = ApplinkConstInternalGlobal.PAYMENT_SETTING, title = "Pembayaran Instan", body = "OVO, kartu kredit, & debit instan terdaftar", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_card),
                        CommonDataView(applink = ApplinkConstInternalMarketplace.PREFERENCE_LIST, title = "Beli Langsung", body = "Atur pengiriman & pembayaran favorit", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_quick_buy),
                        CommonDataView(applink = ApplinkConstInternalGlobal.ACCOUNT_SETTING, title = "Keamanan Akun", body = "Kata sandi, PIN , & verifikasi data diri", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_lock, id = AccountConstants.SettingCode.SETTING_SECURITY),
                        CommonDataView(applink = ApplinkConst.NOTIFICATION, title = "Notifikasi", body = "Atur segala jenis pesan notifikasi", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_ring, id = AccountConstants.SettingCode.SETTING_NOTIFICATION)
                ), isExpanded = true)
    }

    private fun getSettingApp(){
        _settingApplication.value =
                SettingDataView("Pengaturan Aplikasi", mutableListOf(
                        CommonDataView(id = AccountConstants.SettingCode.SETTING_SHAKE_ID, title = "Shake Shake", body = "Goyang HP untuk dapat promo menarik",
                                type = CommonViewHolder.TYPE_SWITCH, icon = R.drawable.ic_account_shake,
                                isChecked = accountPref.isItemSelected(AccountConstants.KEY.KEY_PREF_SHAKE, true)),
                        CommonDataView(id = AccountConstants.SettingCode.SETTING_GEOLOCATION_ID, title = "Geolokasi", body = "Atur rekomendasi berdasarkan lokasi",
                                type = CommonViewHolder.TYPE_SWITCH, icon = R.drawable.ic_account_location,
                                isChecked = permissionChecker.hasLocationPermission()),
                        CommonDataView(id = AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID, title = "Safe Mode", body = "Hasil pencarian aman untuk segala usia", type = CommonViewHolder.TYPE_SWITCH, icon = R.drawable.ic_account_safe_mode,
                                    isChecked = accountPref.isItemSelected(AccountConstants.KEY.KEY_PREF_SAFE_SEARCH, false)),
//                        CommonDataView(title = "Stiker Tokopedia", body = "Tambahkan stike ke WhatsApp & iMessage", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_sticker),
//                        CommonDataView(title = "Kualitas Gambar", body = "Atur kualitas gambar yang dilihat", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_image),
                        CommonDataView(id = AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE, title = "Bersihkan Cache", body = "Solusi cepat untuk atasi masalah aplikasi", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_cache)
//                        CommonDataView(title = "Mode Tampilan", body = "Aktifkan dark mode di Tokopedia", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_dark_mode)
                ), showArrowDown = true)
    }


    private fun getAboutTokopediaData() {
        _aboutTokopedia.value =
                SettingDataView("Seputar Tokopedia", mutableListOf(
                        CommonDataView(title = "Kenali Tokopedia", body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_toped, id = AccountConstants.SettingCode.SETTING_ABOUT_US),
                        CommonDataView(title = "Syarat dan Ketentuan", body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_clipboard, id = AccountConstants.SettingCode.SETTING_TNC_ID),
                        CommonDataView(title = "Kebijakan Privasi", body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_privacy, id = AccountConstants.SettingCode.SETTING_PRIVACY_ID),
                        CommonDataView(title = "Ulas Aplikasi Ini", body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = R.drawable.ic_account_rating, id = AccountConstants.SettingCode.SETTING_APP_REVIEW_ID))
                , showArrowDown = true)
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

    fun getInitialData() {
        getSettingData()
        getSettingApp()
        getAboutTokopediaData()
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

    companion object {
        private const val AKUN_PAGE = "account"
    }

}