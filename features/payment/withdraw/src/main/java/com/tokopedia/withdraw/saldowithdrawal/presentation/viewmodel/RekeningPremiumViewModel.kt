package com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.saldowithdrawal.domain.usecase.GqlRekeningPremiumDataUseCase
import com.tokopedia.withdraw.saldowithdrawal.domain.model.CheckEligible
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RekeningPremiumViewModel @Inject constructor(
        private val gqlRekeningPremiumDataUseCase: GqlRekeningPremiumDataUseCase,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    init {
        loadRekeningPremiumData()
    }

    val rekeningPremiumMutableData = MutableLiveData<Result<CheckEligible>>()

    fun loadRekeningPremiumData() {
        launchCatchError(block = {
            when (val result = gqlRekeningPremiumDataUseCase.getRekeningPremiumData()) {
                is Success -> {
                    onDataLoadedSuccess(result.data.checkEligible)
                }
                is Fail -> {
                    onDataLoadingError(result.throwable)
                }
            }
        }, onError = {
            onDataLoadingError(it)
        })
    }

    private fun onDataLoadedSuccess(checkEligible: CheckEligible) {
        rekeningPremiumMutableData.value = Success(checkEligible)
    }

    private fun onDataLoadingError(throwable: Throwable) {
        rekeningPremiumMutableData.value = Fail(throwable)
    }

}