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
import com.tokopedia.paylater.helper.PdpSimulationException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreditCardViewModel @Inject constructor(
        private val creditCardSimulationUseCase: CreditCardSimulationUseCase,
        private val creditCardPdpMetaInfoUseCase: CreditCardPdpMetaInfoUseCase,
        private val creditCardBankDataUseCase: CreditCardBankDataUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {
    val creditCardSimulationResultLiveData = MutableLiveData<Result<CreditCardSimulationResult>>()
    val creditCardPdpMetaInfoLiveData = MutableLiveData<Result<CreditCardPdpMetaData>>()
    val creditCardBankResultLiveData = MutableLiveData<Result<ArrayList<BankCardListItem>>>()

    fun getCreditCardSimulationData(amount: Float) {
        creditCardSimulationUseCase.cancelJobs()
        if (creditCardSimulationResultLiveData.value !is Success)
            creditCardSimulationUseCase.getCreditCardSimulationData(
                    ::onCreditCardSimulationSuccess,
                    ::onCreditCardSimulationError,
                    amount
            )
    }

    /*  fun getCreditCardData() {
          launchCatchError(block = {
              val creditCardData = withContext(ioDispatcher) {
                  delay(250)
                  return@withContext CreditCardResponseMapper.populateDummyCreditCardData()
              }
              //creditCardSimulationResultLiveData.value = Success(creditCardData)
          }, onError = {
              creditCardSimulationResultLiveData.value = Fail(it)
          })
      }
  */
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
        if (pdpCreditCardSimulationData?.creditCardGetSimulationResult != null) {
            pdpCreditCardSimulationData.creditCardGetSimulationResult.creditCardInstallmentList?.getOrNull(0)?.isSelected = true
            creditCardSimulationResultLiveData.value = Success(pdpCreditCardSimulationData.creditCardGetSimulationResult)
        } else onCreditCardSimulationError(PdpSimulationException.CreditCardNullDataException(SIMULATION_DATA_FAILURE))
        //getCreditCardData()
    }

    private fun onCreditCardSimulationError(throwable: Throwable) {
        //getCreditCardData()
        creditCardSimulationResultLiveData.value = Fail(throwable)
    }

    private fun onPdpInfoMetaDataSuccess(creditCardPdpMetaData: CreditCardPdpMetaData?) {
        if (creditCardPdpMetaData != null && !creditCardPdpMetaData.pdpInfoContentList.isNullOrEmpty())
            launchCatchError(block = {
                withContext(ioDispatcher) {
                    return@withContext CreditCardResponseMapper.populatePdpInfoMetaDataResponse(creditCardPdpMetaData)
                }
                creditCardPdpMetaInfoLiveData.value = Success(creditCardPdpMetaData)
            }, onError = { onPdpInfoMetaDataError(PdpSimulationException.CreditCardNullDataException(CREDIT_CARD_TNC_DATA_FAILURE)) })
        else onPdpInfoMetaDataError(PdpSimulationException.CreditCardNullDataException(CREDIT_CARD_TNC_DATA_FAILURE))
    }

    private fun onPdpInfoMetaDataError(throwable: Throwable) {
        creditCardPdpMetaInfoLiveData.value = Fail(throwable)
    }

    private fun onBankCardListDataSuccess(creditCardBankData: CreditCardBankData?) {
        if (creditCardBankData == null || creditCardBankData.bankCardList.isNullOrEmpty())
            onBankCardListDataError(PdpSimulationException.PayLaterNullDataException(BANK_CARD_DATA_FAILURE))
        else
            creditCardBankResultLiveData.value = Success(creditCardBankData.bankCardList)
    }

    private fun onBankCardListDataError(throwable: Throwable) {
        creditCardBankResultLiveData.value = Fail(throwable)
    }

    fun getRedirectionUrl(): String {
        if (creditCardPdpMetaInfoLiveData.value is Success)
            return (creditCardPdpMetaInfoLiveData.value as Success).data.ctaRedirectionAppLink ?: ""
        return ""
    }

    override fun onCleared() {
        creditCardSimulationUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val SIMULATION_DATA_FAILURE = "NULL DATA"
        const val CREDIT_CARD_TNC_DATA_FAILURE = "NULL DATA"
        const val BANK_CARD_DATA_FAILURE = "NULL_DATA"
        const val PAY_LATER_NOT_APPLICABLE = "PayLater Not Applicable"
    }
}