package com.tokopedia.profilecompletion.addpin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.addpin.data.usecase.CreatePinV2UseCase
import com.tokopedia.profilecompletion.changepin.data.model.MutatePinV2Data
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Param
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-09-02.
 * ade.hadian@tokopedia.com
 */

class AddChangePinViewModel @Inject constructor(
    private val addPinUseCase: GraphqlUseCase<AddPinPojo>,
    private val createPinV2UseCase: CreatePinV2UseCase,
    private val checkPinUseCase: GraphqlUseCase<CheckPinPojo>,
    private val checkPinV2UseCase: CheckPinV2UseCase,
    private val getStatusPinUseCase: GraphqlUseCase<StatusPinPojo>,
    private val validatePinUseCase: GraphqlUseCase<ValidatePinPojo>,
    private val skipOtpPinUseCase: GraphqlUseCase<SkipOtpPinPojo>,
    private val rawQueries: Map<String, String>,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val pinPreference: PinPreference,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableAddPinResponse = MutableLiveData<Result<AddChangePinData>>()
    val addPinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableAddPinResponse

    private val mutableMutatePin = MutableLiveData<Result<MutatePinV2Data>>()
    val mutatePin: LiveData<Result<MutatePinV2Data>>
        get() = mutableMutatePin

    private val mutableCheckPinResponse = MutableLiveData<Result<CheckPinData>>()
    val checkPinResponse: LiveData<Result<CheckPinData>>
        get() = mutableCheckPinResponse

    private val mutableCheckPinV2Response = MutableLiveData<Result<CheckPinV2Data>>()
    val checkPinV2Response: LiveData<Result<CheckPinV2Data>>
        get() = mutableCheckPinV2Response

    private val mutableGetStatusPinResponse = MutableLiveData<Result<StatusPinData>>()
    val getStatusPinResponse: LiveData<Result<StatusPinData>>
        get() = mutableGetStatusPinResponse

    private val mutableValidatePinResponse = MutableLiveData<Result<ValidatePinData>>()
    val validatePinResponse: LiveData<Result<ValidatePinData>>
        get() = mutableValidatePinResponse

    private val mutableSkipOtpPinResponse = MutableLiveData<Result<SkipOtpPinData>>()
    val skipOtpPinResponse: LiveData<Result<SkipOtpPinData>>
        get() = mutableSkipOtpPinResponse

    var hash = ""

    val loadingState = MutableLiveData<Boolean>()

    fun addPin(token: String) {
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

    fun addPinV2(validateToken: String) {
        launchCatchError(block = {
            val param = CreatePinV2Param(
                pin_token = pinPreference.getTempPin(),
                validateToken = validateToken
            )
            val result = createPinV2UseCase(param).mutatePinV2data
            when {
                result.success -> {
                    pinPreference.clearTempPin()
                    mutableMutatePin.value = Success(result)
                }
                result.errors.isNotEmpty() -> mutableMutatePin.value =
                    Fail(MessageErrorException(result.errors[0].message))
                else -> mutableMutatePin.value = Fail(RuntimeException())
            }
        }, onError = {
            mutableMutatePin.value = Fail(it)
        })
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
                it.data.errorAddChangePinData.isNotEmpty() && it.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableAddPinResponse.value =
                        Fail(MessageErrorException(it.data.errorAddChangePinData[0].message))
                else -> mutableAddPinResponse.value = Fail(RuntimeException())
            }
        }
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

    fun checkPinV2(pin: String) {
        pinPreference.clearTempPin()
        launchCatchError(block = {
            val keyData = getPublicKey()
            val encryptedPin = RsaUtils.encryptWithSalt(pin, keyData.key, salt = OtpConstant.PIN_V2_SALT)
            hash = keyData.hash
            val checkPinParam = CheckPinV2Param(encryptedPin, keyData.hash)
            val checkPinResult = checkPinV2UseCase(checkPinParam).data
            when {
                checkPinResult.valid -> {
                    pinPreference.setTempPin(checkPinResult.pinToken)
                    mutableCheckPinV2Response.value = Success(checkPinResult)
                }
                checkPinResult.errorMessage.isNotEmpty() -> {
                    mutableCheckPinV2Response.value = Success(checkPinResult)
                }
                else -> {
                    mutableCheckPinV2Response.value = Fail(RuntimeException())
                }
            }
        }, onError = {
            mutableCheckPinV2Response.value = Fail(it)
        })
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

    fun getStatusPin() {
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
            if (it.data.errorMessage.isNotEmpty()) {
                mutableGetStatusPinResponse.value =
                    Fail(MessageErrorException(it.data.errorMessage))
            } else {
                mutableGetStatusPinResponse.value = Success(it.data)
            }
        }
    }

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

    fun checkSkipOtpPin(validateToken: String) {
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
                    mutableSkipOtpPinResponse.value =
                        Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableSkipOtpPinResponse.value = Success(it.data)
            }
        }
    }

    suspend fun getPublicKey(): KeyData {
        generatePublicKeyUseCase.setParams(SessionConstants.GenerateKeyModule.PIN_V2.value)
        return generatePublicKeyUseCase.executeOnBackground().keyData
    }

    companion object {
        const val OTP_TYPE_SKIP_VALIDATION = 124
    }
}