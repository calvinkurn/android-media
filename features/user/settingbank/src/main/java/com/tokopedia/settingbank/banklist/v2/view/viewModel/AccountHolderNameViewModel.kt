package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewState.AccountNameTextWatcherState
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnAccountNameError
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnAccountNameValidated
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountHolderNameViewModel @Inject constructor(dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val textWatcherState = MutableLiveData<AccountNameTextWatcherState>()

    private var job: Job? = Job()

    fun onValidateAccountName(text: String) {
        cancelCurrentJob()
        createNewJob()
        launchCatchError(block = {
            val state = validateAccountName(text, job!!)
            textWatcherState.value = state
        }) {
            it.printStackTrace()
        }
    }

    private suspend fun validateAccountName(name: String, job: Job): AccountNameTextWatcherState =
            withContext(Dispatchers.IO + job) {
                if (!isLengthCorrect(name)) {
                    return@withContext OnAccountNameError("Length must in min 3 and max 128")
                }
                return@withContext OnAccountNameValidated(name)
            }

    private fun isLengthCorrect(text: String): Boolean {
        return when (text.length) {
            in 3..128 -> true
            else -> false
        }
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