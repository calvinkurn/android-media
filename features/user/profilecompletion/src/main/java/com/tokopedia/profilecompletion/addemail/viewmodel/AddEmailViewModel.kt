package com.tokopedia.profilecompletion.addemail.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.data.AddEmailPojo
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.addemail.data.CheckEmailPojo
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_EMAIL
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_OTP_CODE
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.SessionMessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEmailViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                            private val checkEmaillUseCase: GraphqlUseCase<CheckEmailPojo>,
                                            private val addEmaillUseCase: GraphqlUseCase<AddEmailPojo>)
    : BaseViewModel(dispatcher) {

    val mutateCheckEmailResponse = MutableLiveData<Result<String>>()
    val mutateAddEmailResponse = MutableLiveData<Result<AddEmailResult>>()

    fun mutateAddEmail(context: Context, email: String, otpCode: String) {

        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_email)?.let { query ->

            val params = mapOf(PARAM_EMAIL to email,
                    PARAM_OTP_CODE to otpCode)

            addEmaillUseCase.setTypeClass(AddEmailPojo::class.java)
            addEmaillUseCase.setRequestParams(params)
            addEmaillUseCase.setGraphqlQuery(query)

            addEmaillUseCase.execute(
                    onSuccessMutateAddEmail(email),
                    onErrorMutateAddEmail()
            )

        }

    }

    fun checkEmail(context: Context, email: String) {

        GraphqlHelper.loadRawString(context.resources, R.raw.mutation_check_email)?.let { query ->

            val params = mapOf(PARAM_EMAIL to email)

            checkEmaillUseCase.setTypeClass(CheckEmailPojo::class.java)
            checkEmaillUseCase.setRequestParams(params)
            checkEmaillUseCase.setGraphqlQuery(query)

            checkEmaillUseCase.execute(
                    onSuccessMutateCheckEmail(email),
                    onErrorMutateCheckEmail()
            )
        }

    }

    private fun onSuccessMutateCheckEmail(email: String): (CheckEmailPojo) -> Unit {
        return {
            val errorMessage = it.data.errorMessage
            val isSuccess = it.data.isValid

            if (errorMessage.isBlank() && isSuccess) {
                mutateCheckEmailResponse.value = Success(email)
            } else if (!errorMessage.isBlank()) {
                mutateCheckEmailResponse.value = Fail(SessionMessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutateCheckEmailResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorMutateCheckEmail(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateCheckEmailResponse.value = Fail(it)
        }
    }


    private fun onErrorMutateAddEmail(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutateAddEmailResponse.value = Fail(it)
        }
    }

    private fun onSuccessMutateAddEmail(email: String): (AddEmailPojo) -> Unit {
        return {
            val errorMessage = it.data.errorMessage
            val isSuccess = it.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateAddEmailResponse.value = Success(AddEmailResult(it, email))
            } else if (!errorMessage.isBlank()) {
                mutateAddEmailResponse.value = Fail(SessionMessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutateAddEmailResponse.value = Fail(RuntimeException())
            }
        }
    }
}