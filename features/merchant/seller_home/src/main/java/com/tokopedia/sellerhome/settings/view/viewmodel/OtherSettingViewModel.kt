package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.*
import com.tokopedia.sellerhome.settings.view.uimodel.base.DividerType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class OtherSettingViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    companion object {
        private const val SALDO = "Saldo"
        private const val KREDIT_TOPADS = "Kredit TopAds"
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
        private const val PENGATURAN = "Pengaturan"
    }

    private val _settingListLiveData = MutableLiveData<List<SettingUiModel>>()
    private val _saldoBalanceLiveData = MutableLiveData<BalanceUiModel>()
    private val _kreditTopAdsBalanceLiveData = MutableLiveData<BalanceUiModel>()

    val settingListLiveData: LiveData<List<SettingUiModel>>
        get() = _settingListLiveData
    val saldoBalanceLiveData: LiveData<BalanceUiModel>
        get() = _saldoBalanceLiveData
    val kreditTopAdsBalanceLiveData: LiveData<BalanceUiModel>
        get() = _kreditTopAdsBalanceLiveData

    fun populateAdapterList() {
        val settingList = listOf(
                ShopInfoUiModel(),
                BalanceUiModel(SALDO),
                BalanceUiModel(KREDIT_TOPADS),
                DividerUiModel(),
                SettingTitleUiModel(TINGKATKAN_PENJUALAN),
                MenuItemUiModel(STATISTIK_TOKO, R.drawable.ic_statistic_setting),
                MenuItemUiModel(IKLAN_DAN_PROMOSI_TOKO, R.drawable.ic_ads_promotion),
                SettingTitleUiModel(KABAR_PEMBELI),
                MenuItemUiModel(ULASAN, R.drawable.ic_star_setting),
                MenuItemUiModel(DISKUSI, R.drawable.ic_setting_discussion),
                MenuItemUiModel(KOMPLAIN, R.drawable.ic_complaint),
                DividerUiModel(),
                MenuItemUiModel(LAYANAN_KEUANGAN, R.drawable.ic_finance),
                MenuItemUiModel(PUSAT_EDUKASI_SELLER, R.drawable.ic_seller_edu),
                MenuItemUiModel(TOKOPEDIA_CARE, R.drawable.ic_tokopedia_care),
                DividerUiModel(DividerType.THIN),
                MenuItemUiModel(PENGATURAN, R.drawable.ic_setting)
        )
        _settingListLiveData.value = settingList
    }

}