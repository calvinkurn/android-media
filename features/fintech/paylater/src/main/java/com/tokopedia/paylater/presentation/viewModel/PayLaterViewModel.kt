package com.tokopedia.paylater.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.paylater.data.mapper.PayLaterApplicationStatusMapper
import com.tokopedia.paylater.data.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.paylater.data.mapper.PayLaterSimulationResponseMapper
import com.tokopedia.paylater.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.*
import com.tokopedia.paylater.domain.usecase.PayLaterApplicationStatusUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterProductDetailUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterSimulationUseCase
import com.tokopedia.paylater.helper.PdpSimulationException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
        private val payLaterProductDetailUseCase: PayLaterProductDetailUseCase,
        private val payLaterApplicationStatusUseCase: PayLaterApplicationStatusUseCase,
        private val payLaterSimulationDataUseCase: PayLaterSimulationUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    val payLaterActivityResultLiveData = MutableLiveData<Result<PayLaterProductData>>()
    val payLaterApplicationStatusResultLiveData = MutableLiveData<Result<UserCreditApplicationStatus>>()
    val payLaterSimulationResultLiveData = MutableLiveData<Result<ArrayList<PayLaterSimulationGatewayItem>>>()

    fun getPayLaterProductData() {
        payLaterProductDetailUseCase.cancelJobs()
        payLaterProductDetailUseCase.getPayLaterData(
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

    // invoke only when amount in 10000..30000000
    fun getPayLaterSimulationData(amount: Int) {
        payLaterSimulationDataUseCase.cancelJobs()
        if (amount in 10000..25000000)
            payLaterSimulationDataUseCase.getSimulationData(
                    ::onPayLaterSimulationDataSuccess,
                    ::onPayLaterSimulationDataError,
                    amount
            )
        else onPayLaterSimulationDataError(PdpSimulationException.PayLaterNotApplicableException(PAY_LATER_NOT_APPLICABLE))
    }


    private fun onPayLaterSimulationDataSuccess(payLaterGetSimulationResponse: PayLaterGetSimulationResponse?) {
        launchCatchError(block = {
            val payLaterGatewayList = withContext(ioDispatcher) {
                return@withContext PayLaterSimulationResponseMapper.handleSimulationResponse(payLaterGetSimulationResponse)
            }
            if (payLaterGatewayList.isNotEmpty())
                payLaterSimulationResultLiveData.value = Success(payLaterGatewayList)
            else onPayLaterSimulationDataError(PdpSimulationException.PayLaterNullDataException(SIMULATION_DATA_FAILURE))
        }, onError = {
            onPayLaterSimulationDataError(it)
        })
    }

    private fun onPayLaterSimulationDataError(throwable: Throwable) {
        payLaterSimulationResultLiveData.value = Fail(throwable)
    }

    fun isPayLaterNotApplicable(): Boolean {
        if (payLaterSimulationResultLiveData.value is Fail) {
            return (payLaterSimulationResultLiveData.value as Fail).throwable.message == PAY_LATER_NOT_APPLICABLE
        } else return false
    }

    private fun onPayLaterApplicationStatusError(throwable: Throwable) {
        payLaterApplicationStatusResultLiveData.value = Fail(throwable)
    }

    private fun onPayLaterApplicationStatusSuccess(userCreditApplicationStatus: UserCreditApplicationStatus) {
        launchCatchError(block = {
            val isResponseValid = withContext(ioDispatcher) {
                return@withContext PayLaterApplicationStatusMapper.handleApplicationStateResponse(userCreditApplicationStatus)
            }
            if (isResponseValid)
                payLaterApplicationStatusResultLiveData.value = Success(userCreditApplicationStatus)
            else onPayLaterApplicationStatusError(PdpSimulationException.PayLaterNullDataException(APPLICATION_STATE_DATA_FAILURE))
        }, onError = { onPayLaterApplicationStatusError(it) })
    }

    private fun onPayLaterDataSuccess(productDataList: PayLaterProductData?) {
        launchCatchError(block = {
            val payLaterData: PayLaterProductData? = withContext(ioDispatcher) {
                return@withContext PayLaterPartnerTypeMapper.validateProductData(productDataList)
            }
            payLaterData?.also {
                payLaterActivityResultLiveData.value = Success(it)
            }
                    ?: onPayLaterDataError(PdpSimulationException.PayLaterNullDataException(PAY_LATER_DATA_FAILURE))
        }, onError = {
            onPayLaterDataError(it)
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

    override fun onCleared() {
        payLaterProductDetailUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val SIMULATION_DATA_FAILURE = "NULL DATA"
        const val PAY_LATER_DATA_FAILURE = "NULL DATA"
        const val APPLICATION_STATE_DATA_FAILURE = "NULL_DATA"
        const val PAY_LATER_NOT_APPLICABLE = "NOT_APPLICABLE"
    }
}