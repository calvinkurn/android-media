package com.tokopedia.profilecompletion.addemail.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_EMAIL
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_OTP_CODE
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN
import com.tokopedia.profilecompletion.domain.AddEmailUseCase
import com.tokopedia.profilecompletion.domain.CheckEmailUseCase
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddEmailViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val checkEmailUseCase: CheckEmailUseCase,
    private val addEmailUseCase: AddEmailUseCase
) : BaseViewModel(dispatcher.main) {

    val mutateCheckEmailResponse = MutableLiveData<Result<String>>()
    val mutateAddEmailResponse = MutableLiveData<Result<AddEmailResult>>()

    fun mutateAddEmail(email: String, otpCode: String, validateToken: String) {
        val params = mapOf(
            PARAM_EMAIL to email,
            PARAM_OTP_CODE to otpCode,
            PARAM_VALIDATE_TOKEN to validateToken,
        )

        launchCatchError(block = {
            val response = addEmailUseCase(params)

            val errorMessage = response.data.errorMessage
            val isSuccess = response.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateAddEmailResponse.value = Success(AddEmailResult(response, email))
            } else if (!errorMessage.isBlank()) {
                mutateAddEmailResponse.value = Fail(
                    MessageErrorException(
                        errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()
                    )
                )
            } else {
                mutateAddEmailResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            mutateAddEmailResponse.value = Fail(it)
        })
    }

    fun checkEmail(email: String) {
        val params = mapOf(PARAM_EMAIL to email)

        launchCatchError(block = {
            val response = checkEmailUseCase(params)

            val errorMessage = response.data.errorMessage
            val isSuccess = response.data.isValid

            if (errorMessage.isBlank() && isSuccess) {
                mutateCheckEmailResponse.value = Success(email)
            } else if (!errorMessage.isBlank()) {
                mutateCheckEmailResponse.value = Fail(
                    MessageErrorException(
                        errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()
                    )
                )
            } else {
                mutateCheckEmailResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            mutateCheckEmailResponse.value = Fail(it)
        })
    }

}
