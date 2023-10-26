package com.tokopedia.home_account.account_settings.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSettingViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableLiveData<AccountSettingUiModel> = MutableLiveData()
    val state: LiveData<AccountSettingUiModel>
        get() = _state

    init {
        _state.value = AccountSettingUiModel.Loading
        viewModelScope.launch {
            delay(3000)
            _state.value = AccountSettingUiModel.Not
        }
    }
}

interface AccountSettingUiModel {
    object Loading : AccountSettingUiModel
    object Not : AccountSettingUiModel
}
