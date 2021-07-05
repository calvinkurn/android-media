package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.util.getBankTypeFromAbbreviation
import com.tokopedia.settingbank.view.viewState.OnNOBankSelected
import com.tokopedia.settingbank.view.viewState.ValidateAccountNumberState
import com.tokopedia.settingbank.view.viewState.ValidateAccountNumberSuccess
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ValidateAccountNumberUseCase @Inject constructor() : UseCase<ValidateAccountNumberState>() {

    private val BANK = "Bank"
    private val ACCOUNT_NUMBER = "ACCOUNT_NUMBER"


    fun onTextChanged(bank: Bank?,
                      accountNumber: String,
                      onSuccess: (ValidateAccountNumberState) -> Unit) {
        if (bank == null) {
            onSuccess(OnNOBankSelected)
        } else {
            val requestParams = RequestParams().apply {
                putObject(BANK, bank)
                putString(ACCOUNT_NUMBER, accountNumber)
            }
            execute({
                onSuccess(it)
            }, {

            }, requestParams)
        }
    }

    override suspend fun executeOnBackground(): ValidateAccountNumberState {
        val bank: Bank = useCaseRequestParams.getObject(BANK) as Bank
        val accountNumber = useCaseRequestParams.getString(ACCOUNT_NUMBER, "")
        return validateAccountNumber(bank, accountNumber)
    }

    private fun validateAccountNumber(bank: Bank, numberStr: String): ValidateAccountNumberState {
        val abbreviation = bank.abbreviation ?: ""
        return validateBankAccountNumber(abbreviation, numberStr)
    }

    private fun validateBankAccountNumber(abbreviation: String, numberStr: String): ValidateAccountNumberState {
        val bankType = getBankTypeFromAbbreviation(abbreviation)
        return when (numberStr.length) {
            0 -> ValidateAccountNumberSuccess(isCheckEnable = false,
                    isAddBankButtonEnable = false)
            in 1..bankType.count -> ValidateAccountNumberSuccess(isCheckEnable = true,
                    isAddBankButtonEnable = false)
            else -> ValidateAccountNumberSuccess(isCheckEnable = true,
                    isAddBankButtonEnable = false)
        }
    }


}