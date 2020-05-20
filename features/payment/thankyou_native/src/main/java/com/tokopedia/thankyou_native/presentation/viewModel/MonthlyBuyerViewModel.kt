package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.MonthlyNewBuyer
import com.tokopedia.thankyou_native.domain.usecase.CheckWhiteListStatusUseCase
import com.tokopedia.thankyou_native.domain.usecase.GQLMonthlyNewBuyerUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MonthlyBuyerViewModel @Inject constructor(
        private val gqlMonthlyNewBuyerUseCase: GQLMonthlyNewBuyerUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val monthlyNewBuyerResultLiveData = MutableLiveData<Result<MonthlyNewBuyer>>()

    fun getMonthlyBuyerStatus(orderId: String) {
        gqlMonthlyNewBuyerUseCase.getMonthlyNewBuyer(
                ::onMonthlyBuyerStatusLoaded,
                ::onMonthlyBuyerStatusFailed,
                orderId
        )
    }

    private fun onMonthlyBuyerStatusLoaded(monthlyNewBuyer: MonthlyNewBuyer) {
        monthlyNewBuyerResultLiveData.value = Success(monthlyNewBuyer)
    }

    private fun onMonthlyBuyerStatusFailed(throwable: Throwable) {
        monthlyNewBuyerResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        gqlMonthlyNewBuyerUseCase.cancelJobs()
        super.onCleared()
    }

}