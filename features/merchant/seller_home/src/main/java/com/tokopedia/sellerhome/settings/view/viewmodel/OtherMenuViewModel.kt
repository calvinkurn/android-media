package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.domain.mapToGeneralShopInfo
import com.tokopedia.sellerhome.settings.domain.usecase.GetSettingShopInfoUseCase
import com.tokopedia.sellerhome.settings.domain.usecase.GetShopBadgeUseCase
import com.tokopedia.sellerhome.settings.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.DividerType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class OtherMenuViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val userSession: UserSessionInterface,
        private val getSettingShopInfoUseCase: GetSettingShopInfoUseCase,
        private val getShopBadgeUseCase: GetShopBadgeUseCase,
        private val getShopTotalFollowersUseCase: GetShopTotalFollowersUseCase
): BaseViewModel(dispatcher) {

    companion object {
        private const val TINGKATKAN_PENJUALAN = "TINGKATKAN PENJUALAN"
        private const val STATISTIK_TOKO = "Statistik Toko"
        private const val IKLAN_DAN_PROMOSI_TOKO = "Iklan & Promosi Toko"
        private const val KABAR_PEMBELI = "KABAR PEMBELI"
        private const val ULASAN = "Ulasan"
        private const val DISKUSI = "Diskusi"
        private const val KOMPLAIN = "Komplain"
        private const val LAYANAN_KEUANGAN = "Layanan Keuangan"
        private const val PUSAT_EDUKASI_SELLER = "Pusat Edukasi Seller"
        private const val TOKOPEDIA_CARE = "Tokopedia Care"
    }

    private val _settingListLiveData = MutableLiveData<List<SettingUiModel>>()
    private val _generalShopInfoLiveData = MutableLiveData<Result<GeneralShopInfoUiModel>>()
    private val _totalFollowersLiveData = MutableLiveData<Result<Int>>()
    private val _shopBadgeLiveData = MutableLiveData<Result<String>>()

    val settingListLiveData: LiveData<List<SettingUiModel>>
        get() = _settingListLiveData
    val generalShopInfoLiveData: LiveData<Result<GeneralShopInfoUiModel>>
        get() = _generalShopInfoLiveData
    val totalFollowersLiveData: LiveData<Result<Int>>
        get() = _totalFollowersLiveData
    val shopBadgeLiveData: LiveData<Result<String>>
        get() = _shopBadgeLiveData

    fun populateAdapterList() {
        val settingList = mutableListOf(
                DividerUiModel(DividerType.THIN_FULL),
                DividerUiModel(),
                SettingTitleUiModel(TINGKATKAN_PENJUALAN),
                MenuItemUiModel(
                        STATISTIK_TOKO,
                        R.drawable.ic_statistic_setting,
                        ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD),
                MenuItemUiModel(
                        IKLAN_DAN_PROMOSI_TOKO,
                        R.drawable.ic_ads_promotion,
                        null /* TODO("Masukkin applink lw di sini cup, ganti nullnya. Utk saat ini hanya support applink") */),
                SettingTitleUiModel(KABAR_PEMBELI),
                MenuItemUiModel(
                        ULASAN,
                        R.drawable.ic_star_setting,
                        "tokopedia://review"),
                MenuItemUiModel(
                        DISKUSI,
                        R.drawable.ic_setting_discussion,
                        ApplinkConst.TALK),
                MenuItemUiModel(
                        KOMPLAIN,
                        R.drawable.ic_complaint,
                        ""),
                DividerUiModel(),
                MenuItemUiModel(
                        LAYANAN_KEUANGAN,
                        R.drawable.ic_finance),
                MenuItemUiModel(PUSAT_EDUKASI_SELLER, R.drawable.ic_seller_edu),
                MenuItemUiModel(TOKOPEDIA_CARE, R.drawable.ic_tokopedia_care),
                DividerUiModel(DividerType.THIN_PARTIAL)
        )
        _settingListLiveData.value = settingList
    }

    fun getAllSettingShopInfo() {
        userSession.run {
            getSettingShopInfo(userId)
            getShopTotalFollowers(shopId.toInt())
            getShopBadge(shopId.toInt())
        }
    }

    private fun getSettingShopInfo(userId: String) {
        launchCatchError(block = {
            getSettingShopInfoUseCase.params = GetSettingShopInfoUseCase.createRequestParams(userId.toInt())
            val shopInfo = getSettingShopInfoUseCase.executeOnBackground()
            val generalShopInfoUiModel = shopInfo.mapToGeneralShopInfo()
            _generalShopInfoLiveData.value = Success(generalShopInfoUiModel)
        }, onError = {
            _generalShopInfoLiveData.value = Fail(it)
        })
    }

    private fun getShopTotalFollowers(shopId: Int) {
        launchCatchError(block = {
            getShopTotalFollowersUseCase.params = GetShopTotalFollowersUseCase.createRequestParams(shopId)
            val totalFollowers = getShopTotalFollowersUseCase.executeOnBackground()
            _totalFollowersLiveData.value = Success(totalFollowers)
        }, onError = {
            _totalFollowersLiveData.value = Fail(it)
        })
    }

    private fun getShopBadge(shopId: Int) {
        launchCatchError(block = {
            getShopBadgeUseCase.params = GetShopBadgeUseCase.createRequestParams(shopId)
            val shopBadgeUrl = getShopBadgeUseCase.executeOnBackground()
            _shopBadgeLiveData.value = Success(shopBadgeUrl)
        }, onError = {
            _shopBadgeLiveData.value = Fail(it)
        })
    }

}