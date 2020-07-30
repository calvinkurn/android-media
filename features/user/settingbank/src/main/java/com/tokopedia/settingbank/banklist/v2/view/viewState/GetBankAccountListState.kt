package com.tokopedia.settingbank.banklist.v2.view.viewState

import com.tokopedia.settingbank.banklist.v2.domain.BankAccount

sealed class GetBankAccountListState
data class OnShowLoading(val show: Boolean) : GetBankAccountListState()
data class BankAccountListLoadingError(val throwable: Throwable) : GetBankAccountListState()
data class OnBankAccountListLoaded(val bankList: List<BankAccount>, val toastMessage : String) : GetBankAccountListState()
data class NoBankAccountAdded(val toastMessage : String) : GetBankAccountListState()

