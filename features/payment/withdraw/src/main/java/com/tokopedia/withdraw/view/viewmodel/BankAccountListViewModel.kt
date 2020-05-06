package com.tokopedia.withdraw.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.domain.coroutine.usecase.GetBankAccountsUseCase
import com.tokopedia.withdraw.domain.model.BankAccount
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class BankAccountListViewModel @Inject constructor(
        private val getBankAccountsUseCase: GetBankAccountsUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    init {
        loadBankAccountList()
    }

    val bankListResponseMutableData = MutableLiveData<Result<ArrayList<BankAccount>>>()

    fun loadBankAccountList() {
        getBankAccountsUseCase.getBankAccountList(::onBankAccountLoadedSuccess,
                ::onBankAccountLoadingError)
    }

    private fun onBankAccountLoadedSuccess(bankAccountList: ArrayList<BankAccount>) {
        bankListResponseMutableData.value = Success(bankAccountList)
    }

    private fun onBankAccountLoadingError(throwable: Throwable) {
        bankListResponseMutableData.value = Fail(throwable)
    }
}