package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ThanksPageDataViewModel @Inject constructor(
        private val thanksPageDataUseCase: ThanksPageDataUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val thanksPageDataResultLiveData = MutableLiveData<Result<ThanksPageData>>()

    fun getThanksPageData(paymentId: Long, merchant: String) {
        thanksPageDataUseCase.cancelJobs()
        thanksPageDataUseCase.getThankPageData(
                ::onThanksPageDataSuccess,
                ::onThanksPageDataError,
                paymentId,
                merchant
        )
    }

    private fun onThanksPageDataSuccess(thanksPageData: ThanksPageData) {
        thanksPageDataResultLiveData.value = Success(thanksPageData)
    }

    private fun onThanksPageDataError(throwable: Throwable) {
        thanksPageDataResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        thanksPageDataUseCase.cancelJobs()
        super.onCleared()
    }


}