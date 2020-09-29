package com.tokopedia.profilecompletion.changepin.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changepin.data.ChangePin2FAData
import com.tokopedia.profilecompletion.changepin.data.ResetPin2FaPojo
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import com.tokopedia.profilecompletion.changepin.query.ResetPin2FAQuery
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangePinViewModel @Inject constructor(
        private val validatePinUseCase: GraphqlUseCase<ValidatePinPojo>,
        private val checkPinUseCase: GraphqlUseCase<CheckPinPojo>,
        private val checkPin2FAUseCase: GraphqlUseCase<CheckPinPojo>,
        private val resetPinUseCase: GraphqlUseCase<ResetPinResponse>,
        private val resetPin2FAUseCase: GraphqlUseCase<ResetPin2FaPojo>,
        private val changePinUseCase: GraphqlUseCase<ChangePinPojo>,
        private val userSession: UserSessionInterface,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mutableResetPinResponse = MutableLiveData<Result<AddChangePinData>>()
    val resetPinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableResetPinResponse

    private val mutableResetPin2FAResponse = MutableLiveData<Result<ChangePin2FAData>>()
    val resetPin2FAResponse: LiveData<Result<ChangePin2FAData>>
        get() = mutableResetPin2FAResponse

    private val mutableValidatePinResponse = MutableLiveData<Result<ValidatePinData>>()
    val validatePinResponse: LiveData<Result<ValidatePinData>>
        get() = mutableValidatePinResponse

    private val mutableCheckPinResponse = MutableLiveData<Result<CheckPinData>>()
    val checkPinResponse: LiveData<Result<CheckPinData>>
        get() = mutableCheckPinResponse

    private val mutableChangePinResponse = MutableLiveData<Result<AddChangePinData>>()
    val changePinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableChangePinResponse

    fun validatePin(pin: String) {
        rawQueries[ProfileCompletionQueryConstant.QUERY_VALIDATE_PIN]?.let { query ->
            val params = mapOf(ProfileCompletionQueryConstant.PARAM_PIN to pin)

            validatePinUseCase.setTypeClass(ValidatePinPojo::class.java)
            validatePinUseCase.setRequestParams(params)
            validatePinUseCase.setGraphqlQuery(query)
            validatePinUseCase.execute(
                    onSuccessValidatePin(),
                    onErrorValidatePin()
            )
        }
    }

    private fun onSuccessValidatePin(): (ValidatePinPojo) -> Unit {
        return {
            when {
                it.data.valid -> mutableValidatePinResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableValidatePinResponse.value = Success(it.data)
                else -> mutableValidatePinResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorValidatePin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableValidatePinResponse.value = Fail(it)
        }
    }

    fun checkPin2FA(pin: String, validateToken: String, userId: String) {
        val params = mapOf(
                ProfileCompletionQueryConstant.PARAM_PIN to pin,
                ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
                ProfileCompletionQueryConstant.PARAM_ACTION to "reset",
                ProfileCompletionQueryConstant.PARAM_USER_ID to userId.toIntOrZero()
        )

        checkPin2FAUseCase.setTypeClass(CheckPinPojo::class.java)
        checkPin2FAUseCase.setRequestParams(params)
        checkPin2FAUseCase.setGraphqlQuery(ResetPin2FAQuery.checkPinQuery)
        checkPin2FAUseCase.execute(
                onSuccessCheckPin(),
                onErrorCheckPin()
        )

    }

    fun checkPin(pin: String) {
        rawQueries[ProfileCompletionQueryConstant.QUERY_CHECK_PIN]?.let { query ->
            val params = mapOf(ProfileCompletionQueryConstant.PARAM_PIN to pin)

            checkPinUseCase.setTypeClass(CheckPinPojo::class.java)
            checkPinUseCase.setRequestParams(params)
            checkPinUseCase.setGraphqlQuery(query)
            checkPinUseCase.execute(
                    onSuccessCheckPin(),
                    onErrorCheckPin()
            )
        }
    }

    private fun onErrorCheckPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableCheckPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessCheckPin(): (CheckPinPojo) -> Unit {
        return {
            when {
                it.data.valid -> mutableCheckPinResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableCheckPinResponse.value = Success(it.data)
                else -> mutableCheckPinResponse.value = Fail(RuntimeException())
            }
        }
    }

    fun resetPin2FA(userId: String, validateToken: String) {
        val params = mapOf(
                ProfileCompletionQueryConstant.PARAM_USER_ID to userId.toIntOrZero(),
                ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
                ProfileCompletionQueryConstant.PARAM_GRANT_TYPE to "extension"
        )
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        resetPin2FAUseCase.setTypeClass(ResetPin2FaPojo::class.java)
        resetPin2FAUseCase.setRequestParams(params)
        resetPin2FAUseCase.setGraphqlQuery(ResetPin2FAQuery.resetQuery)
        resetPin2FAUseCase.execute(
                onSuccessResetPin2FA(),
                onErrorResetPin()
        )
    }

    private fun onSuccessResetPin2FA(): (ResetPin2FaPojo) -> Unit {
        return {
            when {
                it.data.is_success == 1 -> mutableResetPin2FAResponse.value = Success(it.data)
                it.data.error.isNotEmpty() ->
                    mutableResetPin2FAResponse.value = Fail(MessageErrorException(it.data.error))
                else -> mutableResetPin2FAResponse.value = Fail(RuntimeException())
            }
        }
    }

    fun resetPin(validateToken: String?) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_RESET_PIN]?.let { query ->
            val params = mapOf(
                    ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to validateToken)
            resetPinUseCase.setTypeClass(ResetPinResponse::class.java)
            resetPinUseCase.setRequestParams(params)
            resetPinUseCase.setGraphqlQuery(query)
            resetPinUseCase.execute(
                    onSuccessResetPin(),
                    onErrorResetPin()
            )
        }
    }

    private fun onErrorResetPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableResetPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessResetPin(): (ResetPinResponse) -> Unit {
        return {
            when {
                it.data.success -> mutableResetPinResponse.value = Success(it.data)
                it.data.errorAddChangePinData.isNotEmpty() && it.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableResetPinResponse.value = Fail(MessageErrorException(it.data.errorAddChangePinData[0].message))
                else -> mutableResetPinResponse.value = Fail(RuntimeException())
            }
        }
    }

    fun changePin(pin: String, pinConfirm: String, pinOld: String) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_UPDATE_PIN]?.let { query ->
            val params = mapOf(
                    ProfileCompletionQueryConstant.PARAM_PIN to pin,
                    ProfileCompletionQueryConstant.PARAM_PIN_CONFIRM to pinConfirm,
                    ProfileCompletionQueryConstant.PARAM_PIN_OLD to pinOld)

            changePinUseCase.setTypeClass(ChangePinPojo::class.java)
            changePinUseCase.setRequestParams(params)
            changePinUseCase.setGraphqlQuery(query)
            changePinUseCase.execute(
                    onSuccessChangePin(),
                    onErrorChangePin()
            )
        }
    }

    private fun onSuccessChangePin(): (ChangePinPojo) -> Unit {
        return {
            when {
                it.data.success -> mutableChangePinResponse.value = Success(it.data)
                it.data.errorAddChangePinData.isNotEmpty() && it.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableChangePinResponse.value = Fail(MessageErrorException(it.data.errorAddChangePinData[0].message))
                else -> mutableChangePinResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorChangePin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableChangePinResponse.value = Fail(it)
        }
    }
}