package com.tokopedia.profilecompletion.addpin.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
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
        private val changePinUseCase: GraphqlUseCase<ChangePinPojo>,
        private val checkPinUseCase: GraphqlUseCase<CheckPinPojo>,
        private val getStatusPinUseCase: GraphqlUseCase<StatusPinPojo>,
        private val validatePinUseCase: GraphqlUseCase<ValidatePinPojo>,
        private val rawQueries: Map<String, String>,
        val dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    private val mutableAddPinResponse = MutableLiveData<Result<AddChangePinData>>()
    val addPinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableAddPinResponse

    private val mutableChangePinResponse = MutableLiveData<Result<AddChangePinData>>()
    val changePinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableChangePinResponse

    private val mutableCheckPinResponse = MutableLiveData<Result<CheckPinData>>()
    val checkPinResponse: LiveData<Result<CheckPinData>>
        get() = mutableCheckPinResponse

    private val mutableGetStatusPinResponse = MutableLiveData<Result<StatusPinData>>()
    val getStatusPinResponse: LiveData<Result<StatusPinData>>
        get() = mutableGetStatusPinResponse

    private val mutableValidatePinResponse = MutableLiveData<Result<ValidatePinData>>()
    val validatePinResponse: LiveData<Result<ValidatePinData>>
        get() = mutableValidatePinResponse

    fun addPin(token: String){
        rawQueries[ProfileCompletionQueriesConstant.MUTATION_CREATE_PIN]?.let { query ->
            val params = mapOf(ProfileCompletionQueriesConstant.PARAM_TOKEN to token)

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

    fun changePin(pin: String, pinConfirm: String, pinOld: String){
        rawQueries[ProfileCompletionQueriesConstant.MUTATION_UPDATE_PIN]?.let { query ->
            val params = mapOf(
                    ProfileCompletionQueriesConstant.PARAM_PIN to pin,
                    ProfileCompletionQueriesConstant.PARAM_PIN_CONFIRM to pinConfirm,
                    ProfileCompletionQueriesConstant.PARAM_PIN_OLD to pinOld)

            changePinUseCase.setTypeClass(ChangePinPojo::class.java)
            changePinUseCase.setRequestParams(params)
            changePinUseCase.setGraphqlQuery(query)
            changePinUseCase.execute(
                    onSuccessChangePin(),
                    onErrorChangePin()
            )
        }
    }

    private fun onErrorChangePin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableChangePinResponse.value = Fail(it)
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

    fun checkPin(pin: String){
        rawQueries[ProfileCompletionQueriesConstant.QUERY_CHECK_PIN]?.let { query ->
            val params = mapOf(ProfileCompletionQueriesConstant.PARAM_PIN to pin)

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
        rawQueries[ProfileCompletionQueriesConstant.QUERY_STATUS_PIN]?.let { query ->
            getStatusPinUseCase.setTypeClass(StatusPinPojo::class.java)
            getStatusPinUseCase.setGraphqlQuery(query)
            getStatusPinUseCase.execute(
                    onSuccessGetStatusPin(),
                    onErrorGetStatusPin()
            )
        }
    }

    private fun onErrorGetStatusPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableGetStatusPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetStatusPin(): (StatusPinPojo) -> Unit {
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
        rawQueries[ProfileCompletionQueriesConstant.QUERY_VALIDATE_PIN]?.let { query ->
            val params = mapOf(ProfileCompletionQueriesConstant.PARAM_PIN to pin)

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
}