package com.tokopedia.payment.setting.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.payment.setting.list.domain.GetCreditCardListUseCase
import com.tokopedia.payment.setting.list.model.PaymentQueryResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SettingsListViewModel @Inject constructor(
        private val getCreditCardListUseCase: GetCreditCardListUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _paymentQueryResultLiveData = MutableLiveData<Result<PaymentQueryResponse>>()
    private val _phoneVerificationStatusLiveData = MutableLiveData<Boolean>()
    val paymentQueryResultLiveData: LiveData<Result<PaymentQueryResponse>> = _paymentQueryResultLiveData
    val phoneVerificationStatusLiveData: LiveData<Boolean> = _phoneVerificationStatusLiveData

    fun getCreditCardList() {
        getCreditCardListUseCase.cancelJobs()
        getCreditCardListUseCase.getCreditCardList(
                ::onCreditCardListSuccess,
                ::onCreditCardListError,
        )
    }

    fun checkVerificationPhone() {
        _phoneVerificationStatusLiveData.value = userSession.isMsisdnVerified
    }

    private fun onCreditCardListSuccess(paymentQueryResponse: PaymentQueryResponse) {
        _paymentQueryResultLiveData.value = Success(paymentQueryResponse)
    }

    private fun onCreditCardListError(throwable: Throwable) {
        _paymentQueryResultLiveData.value = Fail(throwable)
    }

     override fun onCleared() {
         getCreditCardListUseCase.cancelJobs()
     }
}