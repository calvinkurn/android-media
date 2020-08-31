package com.tokopedia.profilecompletion.changepin.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangePinViewModel @Inject constructor(
        private val validatePinUseCase: GraphqlUseCase<ValidatePinPojo>,
        private val checkPinUseCase: GraphqlUseCase<CheckPinPojo>,
        private val resetPinUseCase: GraphqlUseCase<ResetPinResponse>,
        private val changePinUseCase: GraphqlUseCase<ChangePinPojo>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val mutableResetPinResponse = MutableLiveData<Result<AddChangePinData>>()
    val resetPinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableResetPinResponse

    private val mutableValidatePinResponse = MutableLiveData<Result<ValidatePinData>>()
    val validatePinResponse: LiveData<Result<ValidatePinData>>
        get() = mutableValidatePinResponse

    private val mutableCheckPinResponse = MutableLiveData<Result<CheckPinData>>()
    val checkPinResponse: LiveData<Result<CheckPinData>>
        get() = mutableCheckPinResponse

    private val mutableChangePinResponse = MutableLiveData<Result<AddChangePinData>>()
    val changePinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableChangePinResponse

    fun validatePin(pin: String){
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

    fun checkPin(pin: String){
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

    fun resetPin(validateToken: String?){
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

    fun changePin(pin: String, pinConfirm: String, pinOld: String){
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