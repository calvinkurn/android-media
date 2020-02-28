package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.sellerhome.settings.view.uimodel.BalanceUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class OtherSettingViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _settingListLiveData = MutableLiveData<List<SettingUiModel>>()
    private val _saldoBalanceLiveData = MutableLiveData<Int>()

    val saldoBalanceLiveData: LiveData<Int>
        get() = _saldoBalanceLiveData
    val settingListLiveData: LiveData<List<SettingUiModel>>
        get() = _settingListLiveData

    fun populateDummyList() {
        val settingList = listOf<SettingUiModel>(
                BalanceUiModel()
        )
        _settingListLiveData.value = settingList
    }

}