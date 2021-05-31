package com.tokopedia.payment.setting.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.payment.setting.detail.domain.GQLDeleteCreditCardQueryUseCase
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DetailCreditCardViewModel @Inject constructor(
        private val gqlDeleteCreditCardUseCase: GQLDeleteCreditCardQueryUseCase,
        dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _creditCardDeleteResultLiveData = MutableLiveData<Result<DataResponseDeleteCC>>()
    val creditCardDeleteResultLiveData: LiveData<Result<DataResponseDeleteCC>> = _creditCardDeleteResultLiveData
    var tokenId: String = ""

    fun deleteCreditCard(tokenId: String) {
        this.tokenId = tokenId
        gqlDeleteCreditCardUseCase.cancelJobs()
        gqlDeleteCreditCardUseCase.deleteCreditCard(
                ::onCreditCardDeleteSuccess,
                ::onCreditCardDeleteError,
                tokenId
        )
    }

    private fun onCreditCardDeleteSuccess(dataResponseDeleteCC: DataResponseDeleteCC?) {
        dataResponseDeleteCC?.let {
            _creditCardDeleteResultLiveData.value = Success(it)
        } ?: onCreditCardDeleteError(NullPointerException())

    }

    private fun onCreditCardDeleteError(throwable: Throwable) {
        _creditCardDeleteResultLiveData.value = Fail(throwable)
    }

     override fun onCleared() {
         gqlDeleteCreditCardUseCase.cancelJobs()
     }
}