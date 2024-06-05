package com.tokopedia.accountprofile.settingprofile.addemail.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.accountprofile.settingprofile.addemail.data.AddEmailResult
import com.tokopedia.accountprofile.data.AddEmailParam
import com.tokopedia.accountprofile.domain.AddEmailUseCase
import com.tokopedia.accountprofile.domain.CheckEmailUseCase
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
        val params = AddEmailParam(
            email = email,
            otpCode = otpCode,
            validateToken = validateToken,
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
        launchCatchError(block = {
            val response = checkEmailUseCase(email)

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
