package com.tokopedia.settingbank.banklist.v2.view.viewState

import com.tokopedia.settingbank.banklist.v2.domain.AddBankResponse

sealed class AddAccountState
object OnAddAccountNetworkError : AddAccountState()
data class OnSuccessfullyAdded(val addBankResponse: AddBankResponse) : AddAccountState()
data class OnAccountAddingError(val message: String) : AddAccountState()
