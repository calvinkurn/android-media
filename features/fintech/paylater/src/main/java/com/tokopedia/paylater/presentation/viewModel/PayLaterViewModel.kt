package com.tokopedia.paylater.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.paylater.data.mapper.CreditCardResponseMapper
import com.tokopedia.paylater.data.mapper.PayLaterApplicationStatusMapper
import com.tokopedia.paylater.data.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.paylater.data.mapper.PayLaterSimulationResponseMapper
import com.tokopedia.paylater.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.*
import com.tokopedia.paylater.domain.usecase.PayLaterApplicationStatusUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterProductDetailUseCase
import com.tokopedia.paylater.domain.usecase.PayLaterSimulationUseCase
import com.tokopedia.paylater.helper.PayLaterException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
        private val payLaterProductDetailUseCase: PayLaterProductDetailUseCase,
        private val payLaterApplicationStatusUseCase: PayLaterApplicationStatusUseCase,
        private val payLaterSimulationDataUseCase: PayLaterSimulationUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val payLaterActivityResultLiveData = MutableLiveData<Result<PayLaterProductData>>()
    val payLaterApplicationStatusResultLiveData = MutableLiveData<Result<UserCreditApplicationStatus>>()
    val payLaterSimulationResultLiveData = MutableLiveData<Result<ArrayList<PayLaterSimulationGatewayItem>>>()
    val creditCardSimulationResultLiveData = MutableLiveData<Result<ArrayList<SimulationTableResponse>>>()


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

    /**
     * invoke only when amount in 10000..30000000
     */
    fun getPayLaterSimulationData(amount: Int = 1000000) {
        payLaterSimulationDataUseCase.cancelJobs()
        if (amount in 10000..25000000)
            payLaterSimulationDataUseCase.getSimulationData(
                    ::onSimulationDataSuccess,
                    ::onSimulationDataError,
                    amount
            )
        else onSimulationDataError(PayLaterException.PayLaterNotApplicableException(PAY_LATER_NOT_APPLICABLE))
    }

    fun getCreditCardData() {
        launchCatchError(block = {
            val creditCardData = withContext(ioDispatcher) {
                delay(250)
                return@withContext CreditCardResponseMapper.populateDummyCreditCardData()
            }
            creditCardSimulationResultLiveData.value = Success(creditCardData)
        }, onError = {
            creditCardSimulationResultLiveData.value = Fail(it)
        })
    }

    private fun onSimulationDataSuccess(payLaterGetSimulationResponse: PayLaterGetSimulationResponse?) {
        launchCatchError(block = {
            val payLaterGatewayList = withContext(ioDispatcher) {
                return@withContext PayLaterSimulationResponseMapper.handleSimulationResponse(payLaterGetSimulationResponse)
            }
            if (payLaterGatewayList.isNotEmpty())
                payLaterSimulationResultLiveData.value = Success(payLaterGatewayList)
            else onSimulationDataError(PayLaterException.PayLaterNullDataException(SIMULATION_DATA_FAILURE))
        }, onError = {
            onSimulationDataError(it)
        })
    }

    private fun onSimulationDataError(throwable: Throwable) {
        payLaterSimulationResultLiveData.value = Fail(throwable)
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
            else onPayLaterApplicationStatusError(PayLaterException.PayLaterNullDataException(APPLICATION_STATE_DATA_FAILURE))
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
                    ?: onPayLaterDataError(PayLaterException.PayLaterNullDataException(PAY_LATER_DATA_FAILURE))
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
        const val PAY_LATER_NOT_APPLICABLE = "PayLater Not Applicable"
    }
}