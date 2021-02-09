package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.settingbank.view.viewState.AccountNameValidationResult
import com.tokopedia.settingbank.view.viewState.OnAccountNameValidated
import com.tokopedia.settingbank.view.viewState.OnAccountValidationFailed
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ValidateAccountNameUseCase @Inject constructor(): UseCase<AccountNameValidationResult>() {

    private val ACCOUNT_NAME_CHARACTER_RANGE = 3..128
    private val ARG_NAME: String = "arg_name"

    fun validateAccountHolderNameLength(accountHolderName: String,
                                        onComplete: (AccountNameValidationResult) -> Unit) {
        useCaseRequestParams = RequestParams().apply {
            putString(ARG_NAME, accountHolderName)
        }
        execute({
            onComplete(it)
        }, {
            it.printStackTrace()
        }, useCaseRequestParams)
    }


    override suspend fun executeOnBackground(): AccountNameValidationResult {
        val accountHolderName = useCaseRequestParams.getString(ARG_NAME, "")
        if (!isLengthCorrect(accountHolderName)) {
            return OnAccountValidationFailed
        }
        return OnAccountNameValidated(accountHolderName)
    }

    private fun isLengthCorrect(text: String): Boolean {
        return when (text.length) {
            in ACCOUNT_NAME_CHARACTER_RANGE -> true
            else -> false
        }
    }
}