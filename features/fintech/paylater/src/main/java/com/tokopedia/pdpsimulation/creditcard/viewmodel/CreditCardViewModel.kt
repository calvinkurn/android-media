package com.tokopedia.pdpsimulation.creditcard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdpsimulation.common.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.creditcard.domain.model.BankCardListItem
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPdpMetaData
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardSimulationResult
import com.tokopedia.pdpsimulation.creditcard.domain.model.PdpCreditCardSimulation
import com.tokopedia.pdpsimulation.creditcard.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CreditCardViewModel @Inject constructor(
        private val creditCardSimulationUseCase: CreditCardSimulationUseCase,
        private val creditCardPdpMetaInfoUseCase: CreditCardPdpMetaInfoUseCase,
        private val creditCardBankDataUseCase: CreditCardBankDataUseCase,
        private val creditCardTncMapperUseCase: CreditCardTncMapperUseCase,
        private val creditCardSimulationMapperUseCase: CreditCardSimulationMapperUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {
    private val _creditCardSimulationResultLiveData = MutableLiveData<Result<CreditCardSimulationResult>>()
    val creditCardSimulationResultLiveData : LiveData<Result<CreditCardSimulationResult>> = _creditCardSimulationResultLiveData
    private val _creditCardPdpMetaInfoLiveData = MutableLiveData<Result<CreditCardPdpMetaData>>()
    val creditCardPdpMetaInfoLiveData : LiveData<Result<CreditCardPdpMetaData>> = _creditCardPdpMetaInfoLiveData
    private val _creditCardBankResultLiveData = MutableLiveData<Result<ArrayList<BankCardListItem>>>()
    val creditCardBankResultLiveData : LiveData<Result<ArrayList<BankCardListItem>>> = _creditCardBankResultLiveData

    fun getCreditCardSimulationData(amount: Long) {
        creditCardSimulationUseCase.cancelJobs()
        if (creditCardSimulationResultLiveData.value !is Success) {
            creditCardSimulationUseCase.getCreditCardSimulationData(
                    ::onCreditCardSimulationSuccess,
                    ::onCreditCardSimulationError,
                    amount
            )
        }
    }

    fun getCreditCardTncData() {
        creditCardPdpMetaInfoUseCase.cancelJobs()
        if (creditCardPdpMetaInfoLiveData.value !is Success)
            creditCardPdpMetaInfoUseCase.getPdpMetaData(
                    ::onPdpInfoMetaDataSuccess,
                    ::onPdpInfoMetaDataError
            )
    }

    fun getBankCardList() {
        creditCardBankDataUseCase.cancelJobs()
        if (creditCardBankResultLiveData.value !is Success)
            creditCardBankDataUseCase.getBankCardList(
                    ::onBankCardListDataSuccess,
                    ::onBankCardListDataError
            )
    }

    private fun onCreditCardSimulationSuccess(pdpCreditCardSimulationData: PdpCreditCardSimulation?) {
        creditCardSimulationMapperUseCase.parseSimulationData(pdpCreditCardSimulationData, onSuccess = {
            when (it) {
                is StatusApiSuccess -> _creditCardSimulationResultLiveData.value = Success(it.data)
                StatusApiFail -> onCreditCardSimulationError(PdpSimulationException.CreditCardNullDataException(SIMULATION_DATA_FAILURE))
                StatusCCNotAvailable -> onCreditCardSimulationError(PdpSimulationException.CreditCardSimulationNotAvailableException(CREDIT_CARD_NOT_AVAILABLE))
            }
        }, onError = {
            onCreditCardSimulationError(it)
        })
    }

    private fun onCreditCardSimulationError(throwable: Throwable) {
        _creditCardSimulationResultLiveData.value = Fail(throwable)
    }

    private fun onPdpInfoMetaDataSuccess(creditCardPdpMetaData: CreditCardPdpMetaData?) {
        creditCardTncMapperUseCase.parseTncData(creditCardPdpMetaData, onSuccess = {
            _creditCardPdpMetaInfoLiveData.value = Success(it)
        }, onError = {
            _creditCardPdpMetaInfoLiveData.value = Fail(it)
        })
    }

    private fun onPdpInfoMetaDataError(throwable: Throwable) {
        _creditCardPdpMetaInfoLiveData.value = Fail(throwable)
    }

    private fun onBankCardListDataSuccess(creditCardBankList: ArrayList<BankCardListItem>) {
        _creditCardBankResultLiveData.value = Success(creditCardBankList)
    }

    private fun onBankCardListDataError(throwable: Throwable) {
        _creditCardBankResultLiveData.value = Fail(throwable)
    }

    fun getRedirectionUrl(): String {
        if (creditCardPdpMetaInfoLiveData.value is Success)
            return (creditCardPdpMetaInfoLiveData.value as Success).data.ctaRedirectionUrl ?: ""
        return ""
    }

    override fun onCleared() {
        creditCardSimulationUseCase.cancelJobs()
        creditCardTncMapperUseCase.cancelJobs()
        creditCardBankDataUseCase.cancelJobs()
        creditCardPdpMetaInfoUseCase.cancelJobs()
        creditCardSimulationMapperUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val SIMULATION_DATA_FAILURE = "NULL DATA"
        const val CREDIT_CARD_NOT_AVAILABLE = "Credit Card Not Applicable"
    }
}