package com.tokopedia.thankyou_native.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey
import com.tokopedia.thankyou_native.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.usecase.ThanksPageDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThanksPageDataViewModel @Inject constructor(
        private val thanksPageDataUseCase: ThanksPageDataUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val iODispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

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
        launchCatchError(block = {
            withContext(iODispatcher) {
                thanksPageData.paymentDeductions?.forEach {
                    if (it.itemName == PaymentDeductionKey.REWARDS_POINT) {
                        thanksPageData.paymentMethodCount++
                    }
                }
                thanksPageData.paymentDetails?.apply {
                    thanksPageData.paymentMethodCount += (size - 1)
                }
            }
            thanksPageDataResultLiveData.postValue(Success(thanksPageData))
        }, onError = {
            thanksPageDataResultLiveData.postValue(Fail(it))
        })
    }

    private fun onThanksPageDataError(throwable: Throwable) {
        thanksPageDataResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        thanksPageDataUseCase.cancelJobs()
        super.onCleared()
    }

}