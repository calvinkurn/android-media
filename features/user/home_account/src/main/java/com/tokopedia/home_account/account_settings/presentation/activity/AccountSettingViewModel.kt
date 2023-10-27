package com.tokopedia.home_account.account_settings.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.account_settings.data.model.AccountSettingConfig
import com.tokopedia.home_account.account_settings.data.model.AccountSettingConfigResponse
import com.tokopedia.home_account.account_settings.domain.GetAccountSettingConfigUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import rx.Subscriber
import javax.inject.Inject

class AccountSettingViewModel @Inject constructor(
    private val getAccountSettingConfig: GetAccountSettingConfigUseCase
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
            getAccountSettingConfig.execute(
                GetAccountSettingConfigUseCase.getRequestParam(),
                object : Subscriber<GraphqlResponse>() {
                    override fun onCompleted() {
                        // no op
                    }

                    override fun onError(e: Throwable?) {
                        _state.value = AccountSettingUiModel.Display()
                        _errorToast.value = e ?: Throwable()
                    }

                    override fun onNext(response: GraphqlResponse?) {
                        val result =
                            response?.getData<AccountSettingConfig>(AccountSettingConfig::class.java)?.accountSettingConfig
                        _state.value =
                            AccountSettingUiModel.Display(result ?: AccountSettingConfigResponse())
                    }
                }
            )
        }
    }
}

interface AccountSettingUiModel {
    object Loading : AccountSettingUiModel
    data class Display(val config: AccountSettingConfigResponse = AccountSettingConfigResponse()) :
        AccountSettingUiModel
}
