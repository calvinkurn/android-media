package com.tokopedia.settingbank.view.viewState

import com.tokopedia.settingbank.domain.model.Bank

sealed class BankListState
data class OnBankListLoaded(val bankList : ArrayList<Bank>) : BankListState()
data class OnBankListLoadingError(val throwable: Throwable) : BankListState()
data class OnBankSearchResult(val bankList : ArrayList<Bank>) : BankListState()
