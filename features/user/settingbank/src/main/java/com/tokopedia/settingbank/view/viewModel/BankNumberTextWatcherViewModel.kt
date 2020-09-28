package com.tokopedia.settingbank.view.viewModel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.domain.Bank
import com.tokopedia.settingbank.util.BankAccountNumber
import com.tokopedia.settingbank.view.viewState.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.*
import javax.inject.Inject

class BankNumberTextWatcherViewModel @Inject constructor(dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val textWatcherState = MutableLiveData<TextWatcherState>()

    private var job: Job? = Job()

    private var currentBank: Bank? = null

    fun onBankSelected(bank: Bank?) {
        currentBank = bank
    }

    fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentBank?.let {
                    onTextChanged(it, s.toString())
                } ?: run {
                    textWatcherState.value = OnNOBankSelected
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }
    }

    private fun onTextChanged(bank: Bank, text: String) {
        cancelCurrentJob()
        createNewJob()
        launchCatchError(block = {
            val state = validateAccountNumber(bank, text, job!!)
            textWatcherState.value = state
        }) {
            it.printStackTrace()
        }
    }

    private suspend fun validateAccountNumber(bank: Bank, numberStr: String, job: Job): TextWatcherState =
            withContext(Dispatchers.IO + job) {
                val abbreviation = bank.abbreviation?.let { it } ?: ""
                return@withContext validateBankAccountNumber(abbreviation, numberStr)
            }

    private fun validateBankAccountNumber(abbreviation: String, numberStr: String): TextWatcherState {
        val bankType = getBankTypeFromAbbreviation(abbreviation)

        return when (numberStr.length) {
            0 -> OnTextChanged(isCheckEnable = false, clearAccountHolderName = true,
                    isAddBankButtonEnable = false)
            in 1..bankType.count -> OnTextChanged(isCheckEnable = true, clearAccountHolderName = true,
                    isAddBankButtonEnable = false)
            else -> OnTextChanged(isCheckEnable = true, clearAccountHolderName = true,
                    isAddBankButtonEnable = false)
        }
    }

    internal fun getBankTypeFromAbbreviation(abbreviation: String): BankAccountNumber = when (abbreviation.toUpperCase()) {
        BankAccountNumber.BRI.abbrevation.toUpperCase() -> BankAccountNumber.BRI
        BankAccountNumber.BCA.abbrevation.toUpperCase() -> BankAccountNumber.BCA
        BankAccountNumber.Mandiri.abbrevation.toUpperCase() -> BankAccountNumber.Mandiri
        else -> BankAccountNumber.OTHER
    }

    private fun createNewJob() {
        job = Job()
    }

    private fun cancelCurrentJob() {
        job?.cancel()
    }

    override fun onCleared() {
        cancelCurrentJob()
        super.onCleared()
    }

}
