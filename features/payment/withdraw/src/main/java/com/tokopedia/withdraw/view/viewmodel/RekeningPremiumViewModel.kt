package com.tokopedia.withdraw.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.domain.coroutine.usecase.GetBankAccountsUseCase
import com.tokopedia.withdraw.domain.coroutine.usecase.GqlRekeningPremiumDataUseCase
import com.tokopedia.withdraw.domain.model.BankAccount
import com.tokopedia.withdraw.domain.model.premiumAccount.CheckEligible
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RekeningPremiumViewModel @Inject constructor(
        private val gqlRekeningPremiumDataUseCase: GqlRekeningPremiumDataUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    init {
        loadRekeningPremiumData()
    }

    val rekeningPremiumMutableData = MutableLiveData<Result<CheckEligible>>()

    private fun loadRekeningPremiumData() {
        gqlRekeningPremiumDataUseCase.getRekeningPremiumData(::onDataLoadedSuccess,
                ::onDataLoadingError)
    }

    private fun onDataLoadedSuccess(checkEligible: CheckEligible) {
        rekeningPremiumMutableData.value = Success(checkEligible)
    }

    private fun onDataLoadingError(throwable: Throwable) {
        rekeningPremiumMutableData.value = Fail(throwable)
    }
}