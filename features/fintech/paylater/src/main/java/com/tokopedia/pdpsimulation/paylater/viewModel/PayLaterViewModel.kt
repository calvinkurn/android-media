package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdpsimulation.TkpdIdlingResourceProvider
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdpsimulation.common.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.paylater.domain.model.PaylaterGetSimulationV2
import com.tokopedia.pdpsimulation.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.pdpsimulation.paylater.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
    private val payLaterApplicationStatusUseCase: PayLaterApplicationStatusUseCase,
    private val payLaterApplicationStatusMapperUseCase: PayLaterApplicationStatusMapperUseCase,
    private val paylaterGetSimulationV2usecase: PayLaterSimulationV2UseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _payLaterApplicationStatusResultLiveData =
        MutableLiveData<Result<UserCreditApplicationStatus>>()
    val payLaterApplicationStatusResultLiveData: LiveData<Result<UserCreditApplicationStatus>> =
        _payLaterApplicationStatusResultLiveData

    private val _payLaterOptionsDetailLiveData = MutableLiveData<Result<PaylaterGetSimulationV2>>()
    val payLaterOptionsDetailLiveData: LiveData<Result<PaylaterGetSimulationV2>> =
        _payLaterOptionsDetailLiveData

    var isPayLaterProductActive = false

    private var idlingResourceProvider =
        TkpdIdlingResourceProvider.provideIdlingResource("SIMULATION")


    fun getPayLaterAvailableDetail(price: Long) {
        paylaterGetSimulationV2usecase.cancelJobs()
        if (payLaterOptionsDetailLiveData.value !is Success) {
            paylaterGetSimulationV2usecase.getPayLaterProductDetails(
                ::onAvailableDetailSuccess,
                ::onAvailableDetailFail,
                price
            )
        }

    }

    fun getMockOne(price: Long) {
        paylaterGetSimulationV2usecase.mockData()?.let {
            _payLaterOptionsDetailLiveData.value = Success(it)
        }
    }

    private fun onAvailableDetailFail(throwable: Throwable) {
        _payLaterOptionsDetailLiveData.value = Fail(throwable)
    }

    private fun onAvailableDetailSuccess(paylaterGetSimulationV2: PaylaterGetSimulationV2?) {
        paylaterGetSimulationV2?.let {
            _payLaterOptionsDetailLiveData.value = Success(it)
        }
    }


    fun getPayLaterApplicationStatus(shouldFetch: Boolean = true) {
        idlingResourceProvider?.increment()
        payLaterApplicationStatusUseCase.cancelJobs()
        if (shouldFetch && payLaterApplicationStatusResultLiveData.value !is Success)
            payLaterApplicationStatusUseCase.getPayLaterApplicationStatus(
                ::onPayLaterApplicationStatusSuccess,
                ::onPayLaterApplicationStatusError
            )
        else onPayLaterApplicationStatusError(PdpSimulationException.PayLaterNullDataException(DATA_FAILURE))
    }


    private fun onPayLaterApplicationStatusSuccess(userCreditApplicationStatus: UserCreditApplicationStatus) {
        payLaterApplicationStatusMapperUseCase.mapLabelDataToApplicationStatus(userCreditApplicationStatus, onSuccess = {
            when (it) {
                is StatusAppSuccess -> {
                    isPayLaterProductActive = it.isPayLaterActive
                    idlingResourceProvider?.decrement()
                    _payLaterApplicationStatusResultLiveData.value = Success(it.userCreditApplicationStatus)
                }
                StatusFail -> onPayLaterApplicationStatusError(PdpSimulationException.PayLaterNullDataException(DATA_FAILURE))
            }
        }, onError = {
            onPayLaterApplicationStatusError(it)
        })
    }

    private fun onPayLaterApplicationStatusError(throwable: Throwable) {
        idlingResourceProvider?.decrement()
        _payLaterApplicationStatusResultLiveData.value = Fail(throwable)
    }


    override fun onCleared() {
        payLaterApplicationStatusUseCase.cancelJobs()
        payLaterApplicationStatusMapperUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val DATA_FAILURE = "NULL DATA"
        const val PAY_LATER_NOT_APPLICABLE = "Pay Later Not Applicable"
    }
}