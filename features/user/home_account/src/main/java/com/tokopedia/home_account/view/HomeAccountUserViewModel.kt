package com.tokopedia.home_account.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class HomeAccountUserViewModel @Inject constructor(@Named(SessionModule.SESSION_MODULE)
                                                   private val userSession: UserSessionInterface,
                                                   dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

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

    private fun getProfileData() {
        _profileLiveData.value = ProfileDataView(
                name = userSession.name,
                phone = userSession.phoneNumber,
                email = userSession.email,
                avatar = userSession.profilePicture
        )
    }

    private fun getSettingData() {
        _settingData.value =
                SettingDataView("Pengaturan Akun", listOf(
                        CommonDataView(title = "Daftar Alamat", body = "Atur alamat pengiriman belanjaan", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop),
                        CommonDataView(title = "Rekening Bank", body = "Tarik Saldo Tokopedia ke rekening tujuan", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_bank),
                        CommonDataView(title = "Pembayaran Instan", body = "OVO, kartu kredit, & debit instan terdaftar", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop),
                        CommonDataView(title = "Beli Langsung", body = "Atur pengiriman & pembayaran favorit", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop),
                        CommonDataView(title = "Keamanan Akun", body = "Kata sandi, PIN , & verifikasi data diri", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop),
                        CommonDataView(title = "Notifikasi", body = "Atur segala jenis pesan notifikasi", type = CommonViewHolder.TYPE_DEFAULT, icon = R.drawable.ic_account_shop)
                ))
    }

    private fun getSettingApp(){
        _settingApplication.value =
                SettingDataView("Pengaturan Aplikasi", listOf(
                        CommonDataView(title = "Geolokasi", body = "Atur rekomendasi berdasarkan lokasi", type = CommonViewHolder.TYPE_SWITCH),
                        CommonDataView(title = "Shake Shake", body = "Goyang HP untuk dapat promo menarik", type = CommonViewHolder.TYPE_SWITCH),
                        CommonDataView(title = "Safe Mode", body = "Hasil pencarian aman untuk segala usia", type = CommonViewHolder.TYPE_SWITCH),
                        CommonDataView(title = "Stiker Tokopedia", body = "Tambahkan stike ke WhatsApp & iMessage", type = CommonViewHolder.TYPE_DEFAULT),
                        CommonDataView(title = "Kualitas Gambar", body = "Atur kualitas gambar yang dilihat", type = CommonViewHolder.TYPE_DEFAULT)
                ))
    }

    private fun getAboutTokopediaData() {
        _aboutTokopedia.value =
                SettingDataView("Seputar Tokopedia", listOf(
                        CommonDataView(title = "Kenali Tokopedia", body = "Atur rekomendasi berdasarkan lokasi", type = CommonViewHolder.TYPE_WITHOUT_BODY),
                        CommonDataView(title = "Syarat dan Ketentuan", body = "Goyang HP untuk dapat promo menarik", type = CommonViewHolder.TYPE_WITHOUT_BODY),
                        CommonDataView(title = "Kebijakan Privasi", body = "Hasil pencarian aman untuk segala usia", type = CommonViewHolder.TYPE_WITHOUT_BODY),
                        CommonDataView(title = "Ulas Aplikasi Ini", body = "Tambahkan stike ke WhatsApp & iMessage", type = CommonViewHolder.TYPE_WITHOUT_BODY))
                )
    }

    fun getInitialData() {
        getProfileData()
        getSettingData()
        getSettingApp()
        getAboutTokopediaData()
    }

//
//    fun check(onSuccess: (TwoFactorResult) -> Unit, onError: (Throwable) -> Unit) {
//        if(additionalCheckPreference.get().isNeedCheck() && userSession.get().isLoggedIn) {
//            additionalCheckUseCase.get().getBottomSheetData(onSuccess = {
//                additionalCheckPreference.get().setInterval(it.twoFactorResult?.interval ?: 0)
//                onSuccess(it.twoFactorResult!!)
//            }, onError = {
//                onError(it)
//            })
//        }
//    }
}