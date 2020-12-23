package com.tokopedia.paylater.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.paylater.data.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.paylater.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.paylater.helper.PayLaterHelper
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.domain.usecase.PayLaterApplicationStatusUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext

class PayLaterViewModel @Inject constructor(
        private val payLaterDataUseCase: PayLaterDataUseCase,
        private val payLaterApplicationStatusUseCase: PayLaterApplicationStatusUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val ioDispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    val payLaterActivityResultLiveData = MutableLiveData<Result<PayLaterProductData>>()
    val payLaterApplicationStatusResultLiveData = MutableLiveData<Result<UserCreditApplicationStatus>>()

    fun getPayLaterData() {
        payLaterDataUseCase.cancelJobs()
        payLaterDataUseCase.getPayLaterData(
                ::onPayLaterDataSuccess,
                ::onPayLaterDataError
        )
    }

    fun getPayLaterApplicationStatus() {
        payLaterApplicationStatusUseCase.cancelJobs()
        payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(
                ::onPayLaterApplicationStatusSuccess,
                ::onPayLaterApplicationStatusError
        )
    }

    private fun onPayLaterApplicationStatusError(throwable: Throwable) {
        payLaterApplicationStatusResultLiveData.value = Fail(throwable)
    }

    private fun onPayLaterApplicationStatusSuccess(userCreditApplicationStatus: UserCreditApplicationStatus) {
        for (applicationDetail in userCreditApplicationStatus.applicationDetailList) {
            PayLaterHelper.setLabelData(applicationDetail)
        }
        payLaterApplicationStatusResultLiveData.value = Success(userCreditApplicationStatus)
    }

    private fun onPayLaterDataSuccess(productDataList: PayLaterProductData?) {
        launchCatchError(block = {
            val payLaterData: PayLaterProductData? = withContext(ioDispatcher) {
                return@withContext PayLaterPartnerTypeMapper.validateProductData(productDataList)
            }
            payLaterData?.let {
                payLaterActivityResultLiveData.value = Success(it)
            }
        }, onError = {
            payLaterActivityResultLiveData.value = Fail(it)
        })
    }

    private fun onPayLaterDataError(throwable: Throwable) {
        payLaterActivityResultLiveData.value = Fail(throwable)
    }

    fun getPayLaterOptions(): ArrayList<PayLaterItemProductData> {
        payLaterActivityResultLiveData.value?.let {
            if (it is Success && !it.data.productList.isNullOrEmpty()) {
                return it.data.productList!!
            }
        }
        return arrayListOf()
    }

   /* fun getApplicationStatusData(): ArrayList<PayLaterApplicationDetail> {
        payLaterApplicationStatusResultLiveData.value?.let {
            if (it is Success) return it.data.applicationDetailList
        }
        return ArrayList()
    }
*/
    override fun onCleared() {
        payLaterDataUseCase.cancelJobs()
        super.onCleared()
    }
}