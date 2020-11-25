package com.tokopedia.settingbank.view.viewState

import com.tokopedia.settingbank.domain.AddBankResponse

sealed class AddAccountState
object OnAddBankRequestStarted: AddAccountState()
object OnAddBankRequestEnded: AddAccountState()
data class OnAddAccountNetworkError(val throwable: Throwable): AddAccountState()
data class OnSuccessfullyAdded(val addBankResponse: AddBankResponse) : AddAccountState()
data class OnAccountAddingError(val throwable: Throwable) : AddAccountState()
