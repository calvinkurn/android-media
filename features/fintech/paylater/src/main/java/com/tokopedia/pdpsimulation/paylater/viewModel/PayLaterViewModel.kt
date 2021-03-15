package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdpsimulation.common.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
        private val payLaterProductDetailUseCase: PayLaterProductDetailUseCase,
        private val payLaterApplicationStatusUseCase: PayLaterApplicationStatusUseCase,
        private val payLaterSimulationDataUseCase: PayLaterSimulationUseCase,
        private val payLaterTenureMapperUseCase: PayLaterTenureMapperUseCase,
        private val payLaterApplicationStatusMapperUseCase: PayLaterApplicationStatusMapperUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _payLaterActivityResultLiveData = MutableLiveData<Result<PayLaterProductData>>()
    val payLaterActivityResultLiveData: LiveData<Result<PayLaterProductData>> = _payLaterActivityResultLiveData
    private val _payLaterApplicationStatusResultLiveData = MutableLiveData<Result<UserCreditApplicationStatus>>()
    val payLaterApplicationStatusResultLiveData: LiveData<Result<UserCreditApplicationStatus>> = _payLaterApplicationStatusResultLiveData
    private val _payLaterSimulationResultLiveData = MutableLiveData<Result<ArrayList<PayLaterSimulationGatewayItem>>>()
    val payLaterSimulationResultLiveData: LiveData<Result<ArrayList<PayLaterSimulationGatewayItem>>> = _payLaterSimulationResultLiveData
    var isPayLaterProductActive = false

    fun getPayLaterSimulationData(amount: Int) {
        payLaterSimulationDataUseCase.cancelJobs()
        payLaterSimulationDataUseCase.getSimulationData(
                ::onPayLaterSimulationDataSuccess,
                ::onPayLaterSimulationDataError,
                amount
        )
    }

    fun getPayLaterProductData() {
        payLaterProductDetailUseCase.cancelJobs()
        if (payLaterActivityResultLiveData.value !is Success)
            payLaterProductDetailUseCase.getPayLaterData(
                    ::onPayLaterDataSuccess,
                    ::onPayLaterDataError
            )
    }

    fun getPayLaterApplicationStatus(shouldFetch: Boolean = true) {
        payLaterApplicationStatusUseCase.cancelJobs()
        if (shouldFetch && payLaterApplicationStatusResultLiveData.value !is Success)
            payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(
                    ::onPayLaterApplicationStatusSuccess,
                    ::onPayLaterApplicationStatusError
            )
        else onPayLaterApplicationStatusError(PdpSimulationException.PayLaterNullDataException(DATA_FAILURE))
    }

    private fun onPayLaterSimulationDataSuccess(payLaterGetSimulationResponse: PayLaterGetSimulationResponse?) {
        payLaterTenureMapperUseCase.mapTenureToSimulation(payLaterGetSimulationResponse, onSuccess = {
            when (it) {
                is StatusSuccess -> _payLaterSimulationResultLiveData.value = Success(it.data)
                StatusPayLaterNotAvailable -> onPayLaterSimulationDataError(PdpSimulationException.PayLaterNotApplicableException(PAY_LATER_NOT_APPLICABLE))
                StatusDataFailure -> onPayLaterSimulationDataError(PdpSimulationException.PayLaterNullDataException(DATA_FAILURE))
            }
        }, onError = {
            onPayLaterSimulationDataError(it)
        })
    }

    private fun onPayLaterSimulationDataError(throwable: Throwable) {
        _payLaterSimulationResultLiveData.value = Fail(throwable)
    }

    private fun onPayLaterApplicationStatusSuccess(userCreditApplicationStatus: UserCreditApplicationStatus) {
        payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(userCreditApplicationStatus, onSuccess = {
            when (it) {
                is StatusAppSuccess -> {
                    isPayLaterProductActive = it.isPayLaterActive
                    _payLaterApplicationStatusResultLiveData.value = Success(it.userCreditApplicationStatus)
                }
                StatusFail -> onPayLaterApplicationStatusError(PdpSimulationException.PayLaterNullDataException(DATA_FAILURE))
            }
        }, onError = {
            onPayLaterApplicationStatusError(it)
        })
    }

    private fun onPayLaterApplicationStatusError(throwable: Throwable) {
        _payLaterApplicationStatusResultLiveData.value = Fail(throwable)
    }

    private fun onPayLaterDataSuccess(productDataList: PayLaterProductData) {
        _payLaterActivityResultLiveData.value = Success(productDataList)
    }

    private fun onPayLaterDataError(throwable: Throwable) {
        _payLaterActivityResultLiveData.value = Fail(throwable)
    }

    fun getPayLaterOptions(): ArrayList<PayLaterItemProductData> {
        payLaterActivityResultLiveData.value?.let {
            if (it is Success && !it.data.productList.isNullOrEmpty()) {
                return it.data.productList ?: arrayListOf()
            }
        }
        return arrayListOf()
    }

    override fun onCleared() {
        payLaterProductDetailUseCase.cancelJobs()
        payLaterSimulationDataUseCase.cancelJobs()
        payLaterApplicationStatusUseCase.cancelJobs()
        payLaterTenureMapperUseCase.cancelJobs()
        payLaterApplicationStatusMapperUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val DATA_FAILURE = "NULL DATA"
        const val PAY_LATER_NOT_APPLICABLE = "Pay Later Not Applicable"
    }
}