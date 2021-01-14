package com.tokopedia.paylater.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.paylater.data.mapper.CreditCardResponseMapper
import com.tokopedia.paylater.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.paylater.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.paylater.domain.model.*
import com.tokopedia.paylater.domain.usecase.CreditCardBankDataUseCase
import com.tokopedia.paylater.domain.usecase.CreditCardPdpMetaInfoUseCase
import com.tokopedia.paylater.domain.usecase.CreditCardSimulationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CreditCardViewModel @Inject constructor(
        private val creditCardSimulationUseCase: CreditCardSimulationUseCase,
        private val creditCardPdpMetaInfoUseCase: CreditCardPdpMetaInfoUseCase,
        private val creditCardBankDataUseCase: CreditCardBankDataUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {
    val creditCardSimulationResultLiveData = MutableLiveData<Result<ArrayList<SimulationTableResponse>>>()
    val creditCardPdpMetaInfoLiveData = MutableLiveData<Result<CreditCardPdpMetaData>>()
    val creditCardBankResultLiveData = MutableLiveData<Result<ArrayList<BankCardListItem>>>()

    fun getCreditCardSimulationData(amount: Float) {
        creditCardSimulationUseCase.cancelJobs()
        creditCardSimulationUseCase.getCreditCardSimulationData(
                ::onCreditCardSimulationSuccess,
                ::onCreditCardSimulationError,
                amount
        )
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

    fun getCreditCardTncData() {
        creditCardPdpMetaInfoUseCase.cancelJobs()
        creditCardPdpMetaInfoUseCase.getPdpMetaData(
                ::onPdpInfoMetaDataSuccess,
                ::onPdpInfoMetaDataError
        )
    }

    fun getBankCardList() {
        creditCardBankDataUseCase.cancelJobs()
        creditCardBankDataUseCase.getBankCardList(
                ::onBankCardListDataSuccess,
                ::onBankCardListDataError
        )
    }


    private fun onCreditCardSimulationSuccess(creditCardGetSimulationResponse: CreditCardGetSimulationResponse) {
        Timber.d(creditCardGetSimulationResponse.toString())
        getCreditCardData()
    }

    private fun onCreditCardSimulationError(throwable: Throwable) {
        getCreditCardData()
        //creditCardSimulationResultLiveData.value = Fail(throwable)
    }

    private fun onPdpInfoMetaDataSuccess(creditCardPdpMetaData: CreditCardPdpMetaData?) {
        Timber.d(creditCardPdpMetaData.toString())
    }

    private fun onPdpInfoMetaDataError(throwable: Throwable) {
        //getCreditCardData()
        //creditCardPdpMetaInfoLiveData.value = Fail(throwable)
    }


    private fun onBankCardListDataSuccess(creditCardBankData: CreditCardBankData?) {
        Timber.d(creditCardBankData.toString())
        creditCardBankResultLiveData.value = Success(creditCardBankData?.bankCardList!!)
        //getCreditCardData()
    }

    private fun onBankCardListDataError(throwable: Throwable) {
        //getCreditCardData()
        creditCardBankResultLiveData.value = Fail(throwable)
    }

    override fun onCleared() {
        creditCardSimulationUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val SIMULATION_DATA_FAILURE = "NULL DATA"
        const val PAY_LATER_DATA_FAILURE = "NULL DATA"
        const val APPLICATION_STATE_DATA_FAILURE = "NULL_DATA"
        const val PAY_LATER_NOT_APPLICABLE = "PayLater Not Applicable"
    }
}