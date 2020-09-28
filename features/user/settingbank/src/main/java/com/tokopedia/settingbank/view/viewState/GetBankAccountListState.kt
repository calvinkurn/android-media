package com.tokopedia.settingbank.view.viewState

import com.tokopedia.settingbank.domain.BankAccount

sealed class GetBankAccountListState
data class OnShowLoading(val show: Boolean) : GetBankAccountListState()
data class BankAccountListLoadingError(val throwable: Throwable) : GetBankAccountListState()
data class OnBankAccountListLoaded(val bankList: List<BankAccount>, val toastMessage : String) : GetBankAccountListState()
data class NoBankAccountAdded(val toastMessage : String) : GetBankAccountListState()

