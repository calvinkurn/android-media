package com.tokopedia.settingbank.view.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.view.viewState.AccountNameTextWatcherState
import com.tokopedia.settingbank.view.viewState.OnAccountNameError
import com.tokopedia.settingbank.view.viewState.OnAccountNameValidated
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountHolderNameViewModel @Inject constructor(private val context: Context,
                                                     dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val textWatcherState = MutableLiveData<AccountNameTextWatcherState>()

    private var job: Job? = Job()
    private val ACCOUNT_NAME_CHARACTER_RANGE = 3..128

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
                    return@withContext OnAccountNameError(context.resources.getString(R.string.sbank_name_char_limit_error))
                }
                return@withContext OnAccountNameValidated(name)
            }

    private fun isLengthCorrect(text: String): Boolean {
        return when (text.length) {
            in ACCOUNT_NAME_CHARACTER_RANGE -> true
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