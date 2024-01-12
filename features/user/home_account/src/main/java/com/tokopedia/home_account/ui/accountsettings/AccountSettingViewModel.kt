package com.tokopedia.home_account.ui.accountsettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.home_account.account_settings.data.model.AccountSettingResponse
import com.tokopedia.home_account.domain.usecase.GetAccountSettingUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSettingViewModel @Inject constructor(
    private val getAccountSetting: GetAccountSettingUseCase
) : ViewModel() {

    private val _state: MutableLiveData<AccountSettingUiModel> = MutableLiveData()
    val state: LiveData<AccountSettingUiModel>
        get() = _state

    private val _errorToast: SingleLiveEvent<Throwable> = SingleLiveEvent()
    val errorToast: LiveData<Throwable>
        get() = _errorToast

    init {
        fetch()
    }

    fun fetch() {
        _state.value = AccountSettingUiModel.Loading
        viewModelScope.launch {
            launch {
                try {
                    val result = getAccountSetting(Unit)
                    _state.value = AccountSettingUiModel.Display(result)
                } catch (e: Exception) {
                    _state.value = AccountSettingUiModel.Display()
                    _errorToast.value = e
                }
            }
        }
    }
}

interface AccountSettingUiModel {
    object Loading : AccountSettingUiModel
    data class Display(val config: AccountSettingResponse.Config = AccountSettingResponse.Config()) :
        AccountSettingUiModel
}
