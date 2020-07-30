package com.tokopedia.settingbank.banklist.v2.view.viewState

import com.tokopedia.settingbank.banklist.v2.domain.Bank

sealed class BankListState
object OnBankListLoading  : BankListState()
data class OnBankListLoaded(val bankList : ArrayList<Bank>) : BankListState()
data class OnBankListLoadingError(val throwable: Throwable) : BankListState()
data class OnBankSearchResult(val bankList : ArrayList<Bank>) : BankListState()
