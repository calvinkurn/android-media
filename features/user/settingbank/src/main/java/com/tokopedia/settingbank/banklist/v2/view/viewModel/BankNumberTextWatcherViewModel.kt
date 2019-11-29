package com.tokopedia.settingbank.banklist.v2.view.viewModel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnNoBankSelected
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnTextWatcherError
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnTextWatcherSuccess
import com.tokopedia.settingbank.banklist.v2.view.viewState.TextWatcherState
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.*
import javax.inject.Inject

class BankNumberTextWatcherViewModel @Inject constructor(dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val TEXT_WATCHER_DELAY = 300L

    val textWatcherState = MutableLiveData<TextWatcherState>()

    private var job: Job? = Job()

    private var currentBank: Bank? = null

    fun onBankSelected(bank: Bank?) {
        currentBank = bank
        if(textWatcherState.value == OnNoBankSelected)
            textWatcherState.value = OnTextWatcherSuccess
    }

    fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentBank?.let {
                    onTextChanged(s.toString())
                } ?: run {
                    textWatcherState.value = OnNoBankSelected
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }
    }

    private fun onTextChanged(text: String) {
        cancelCurrentJob()
        createNewJob()
        launchCatchError(block = {
            val state = validateAccountNumber(text, job!!)
            textWatcherState.value = state
        }) {
            it.printStackTrace()
        }
    }

    private suspend fun validateAccountNumber(numberStr: String, job: Job): TextWatcherState =
            withContext(Dispatchers.IO + job) {
                delay(TEXT_WATCHER_DELAY)
                if(numberStr.length > 15){
                    return@withContext OnTextWatcherError("Jumlah angka pada nomor rekening tidak sesuai")
                }
                return@withContext OnTextWatcherSuccess
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