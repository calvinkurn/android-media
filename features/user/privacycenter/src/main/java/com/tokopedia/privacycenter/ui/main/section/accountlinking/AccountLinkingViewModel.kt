package com.tokopedia.privacycenter.ui.main.section.accountlinking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.domain.AccountLinkingStatus
import com.tokopedia.privacycenter.domain.AccountLinkingUseCase
import javax.inject.Inject
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
class AccountLinkingViewModel @Inject constructor(
    private val accountLinkingUseCase: AccountLinkingUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _accountLinkingState = MutableLiveData<PrivacyCenterStateResult<AccountLinkingStatus>>()
    val accountLinkingState: LiveData<PrivacyCenterStateResult<AccountLinkingStatus>> get() = _accountLinkingState

    init {
        getAccountLinkingStatus()
    }

    fun getAccountLinkingStatus() {
        _accountLinkingState.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            val result = accountLinkingUseCase(AccountLinkingUseCase.ACCOUNT_LINKING_TYPE)
            _accountLinkingState.value = result
        }, {
            _accountLinkingState.value = PrivacyCenterStateResult.Fail(it)
        })
    }
}
