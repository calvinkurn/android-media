package com.tokopedia.settingbank.banklist.v2.view.viewState

import com.tokopedia.settingbank.banklist.v2.domain.AddBankResponse

sealed class AddAccountState
object OnAddBankRequestStarted: AddAccountState()
object OnAddBankRequestEnded: AddAccountState()
data class OnAddAccountNetworkError(val throwable: Throwable): AddAccountState()
data class OnSuccessfullyAdded(val addBankResponse: AddBankResponse) : AddAccountState()
data class OnAccountAddingError(val throwable: Throwable) : AddAccountState()
