package com.tokopedia.profilecompletion.addpin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-09-02.
 * ade.hadian@tokopedia.com
 */

class AddChangePinViewModel @Inject constructor(
        private val addPinUseCase: GraphqlUseCase<AddPinPojo>,
        private val checkPinUseCase: GraphqlUseCase<CheckPinPojo>,
        private val getStatusPinUseCase: GraphqlUseCase<StatusPinPojo>,
        private val validatePinUseCase: GraphqlUseCase<ValidatePinPojo>,
        private val skipOtpPinUseCase: GraphqlUseCase<SkipOtpPinPojo>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    private val mutableAddPinResponse = MutableLiveData<Result<AddChangePinData>>()
    val addPinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableAddPinResponse

    private val mutableCheckPinResponse = MutableLiveData<Result<CheckPinData>>()
    val checkPinResponse: LiveData<Result<CheckPinData>>
        get() = mutableCheckPinResponse

    private val mutableGetStatusPinResponse = MutableLiveData<Result<StatusPinData>>()
    val getStatusPinResponse: LiveData<Result<StatusPinData>>
        get() = mutableGetStatusPinResponse

    private val mutableValidatePinResponse = MutableLiveData<Result<ValidatePinData>>()
    val validatePinResponse: LiveData<Result<ValidatePinData>>
        get() = mutableValidatePinResponse

    private val mutableSkipOtpPinResponse = MutableLiveData<Result<SkipOtpPinData>>()
    val skipOtpPinResponse: LiveData<Result<SkipOtpPinData>>
        get() = mutableSkipOtpPinResponse

    val loadingState = MutableLiveData<Boolean>()

    fun addPin(token: String){
        rawQueries[ProfileCompletionQueryConstant.MUTATION_CREATE_PIN]?.let { query ->
            val params = mapOf(ProfileCompletionQueryConstant.PARAM_TOKEN to token)

            addPinUseCase.setTypeClass(AddPinPojo::class.java)
            addPinUseCase.setRequestParams(params)
            addPinUseCase.setGraphqlQuery(query)
            addPinUseCase.execute(
                    onSuccessAddPin(),
                    onErrorAddPin()
            )
        }
    }

    private fun onErrorAddPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableAddPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessAddPin(): (AddPinPojo) -> Unit {
        return {
            when {
                it.data.success -> mutableAddPinResponse.value = Success(it.data)
                it.data.errorAddChangePinData.isNotEmpty() && it.data.errorAddChangePinData[0].message.isNotEmpty()->
                    mutableAddPinResponse.value = Fail(MessageErrorException(it.data.errorAddChangePinData[0].message))
                else -> mutableAddPinResponse.value = Fail(RuntimeException())
            }
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

    fun getStatusPin(){
        loadingState.postValue(true)
        rawQueries[ProfileCompletionQueryConstant.QUERY_GET_STATUS_PIN]?.let { query ->
            getStatusPinUseCase.setTypeClass(StatusPinPojo::class.java)
            getStatusPinUseCase.setGraphqlQuery(query)
            getStatusPinUseCase.execute(
                    onSuccessGetStatusPin(),
                    onErrorGetStatusPin()
            )
        }
    }

    private fun onErrorGetStatusPin(): (Throwable) -> Unit {
        loadingState.postValue(false)
        return {
            it.printStackTrace()
            mutableGetStatusPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetStatusPin(): (StatusPinPojo) -> Unit {
        loadingState.postValue(false)
        return {
            when {
                it.data.errorMessage.isEmpty() -> mutableGetStatusPinResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableGetStatusPinResponse.value = Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableGetStatusPinResponse.value = Fail(RuntimeException())
            }
        }
    }

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

    private fun onErrorValidatePin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableValidatePinResponse.value = Fail(it)
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
  
    fun checkSkipOtpPin(validateToken: String){
        rawQueries[ProfileCompletionQueryConstant.QUERY_SKIP_OTP_PIN]?.let { query ->
            val params = mapOf(
                    ProfileCompletionQueryConstant.PARAM_OTP_TYPE to OTP_TYPE_SKIP_VALIDATION,
                    ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN_SKIP_OTP to validateToken
            )

            skipOtpPinUseCase.setTypeClass(SkipOtpPinPojo::class.java)
            skipOtpPinUseCase.setRequestParams(params)
            skipOtpPinUseCase.setGraphqlQuery(query)
            skipOtpPinUseCase.execute(
                    onSuccessCheckSkipOtpPin(),
                    onErrorCheckSkipOtpPin()
            )
        }
    }

    private fun onErrorCheckSkipOtpPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableSkipOtpPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessCheckSkipOtpPin(): (SkipOtpPinPojo) -> Unit {
        return {
            when {
                it.data.errorMessage.isNotEmpty() ->
                    mutableSkipOtpPinResponse.value = Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableSkipOtpPinResponse.value = Success(it.data)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        addPinUseCase.cancelJobs()
        checkPinUseCase.cancelJobs()
        getStatusPinUseCase.cancelJobs()
        validatePinUseCase.cancelJobs()
        skipOtpPinUseCase.cancelJobs()
    }
  
    companion object {
        const val OTP_TYPE_SKIP_VALIDATION = 124
    }
}