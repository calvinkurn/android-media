package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.BalanceUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel
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
        val settingList = listOf<SettingUiModel>(
                BalanceUiModel(SALDO),
                BalanceUiModel(KREDIT_TOPADS),
                DividerUiModel(),
                SettingTitleUiModel(TINGKATKAN_PENJUALAN),
                MenuItemUiModel("Statistik Toko", R.drawable.ic_arrow_up),
                MenuItemUiModel("Statistik Tokai", R.drawable.ic_arrow_up)
        )
        _settingListLiveData.value = settingList
    }

}