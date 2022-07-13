package com.tokopedia.profilecompletion.changepin.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.profilecompletion.addpin.data.*
import com.tokopedia.profilecompletion.changepin.data.ChangePin2FAData
import com.tokopedia.profilecompletion.changepin.data.ResetPin2FaPojo
import com.tokopedia.profilecompletion.changepin.data.ResetPinResponse
import com.tokopedia.profilecompletion.changepin.data.model.ChangePinV2Param
import com.tokopedia.profilecompletion.changepin.data.model.ResetPinV2Param
import com.tokopedia.profilecompletion.changepin.data.usecase.ResetPinV2UseCase
import com.tokopedia.profilecompletion.changepin.data.usecase.UpdatePinV2UseCase
import com.tokopedia.profilecompletion.changepin.query.ResetPin2FAQuery
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Param
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.constants.SessionConstants.cleanPublicKey
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.pin.PinStatusParam
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Data
import com.tokopedia.sessioncommon.data.pin.ValidatePinV2Param
import com.tokopedia.sessioncommon.domain.usecase.CheckPinHashV2UseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.ValidatePinV2UseCase
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
    private val validatePinV2UseCase: ValidatePinV2UseCase,
    private val checkPinUseCase: GraphqlUseCase<CheckPinPojo>,
    private val checkPin2FAUseCase: GraphqlUseCase<CheckPinPojo>,
    private val resetPinUseCase: GraphqlUseCase<ResetPinResponse>,
    private val resetPinV2UseCase: ResetPinV2UseCase,
    private val resetPin2FAUseCase: GraphqlUseCase<ResetPin2FaPojo>,
    private val changePinUseCase: GraphqlUseCase<ChangePinPojo>,
    private val updatePinV2UseCase: UpdatePinV2UseCase,
    private val checkPinV2UseCase: CheckPinV2UseCase,
    private val userSession: UserSessionInterface,
    private val rawQueries: Map<String, String>,
    private val checkPinHashV2UseCase: CheckPinHashV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val pinPreference: PinPreference,
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

    private val mutableValidatePinV2Response = MutableLiveData<Result<ValidatePinV2Data>>()
    val validatePinV2Response: LiveData<Result<ValidatePinV2Data>>
        get() = mutableValidatePinV2Response

    private val mutableCheckPinResponse = MutableLiveData<Result<CheckPinData>>()
    val checkPinResponse: LiveData<Result<CheckPinData>>
        get() = mutableCheckPinResponse

    private val mutableChangePinResponse = MutableLiveData<Result<AddChangePinData>>()
    val changePinResponse: LiveData<Result<AddChangePinData>>
        get() = mutableChangePinResponse

    private val mutableCheckPinV2Response = MutableLiveData<Result<CheckPinV2Data>>()
    val checkPinV2Response: LiveData<Result<CheckPinV2Data>>
        get() = mutableCheckPinV2Response

    var hash = ""

    fun validatePinMediator(pin: String) {
        launchCatchError(block = {
            val param = PinStatusParam(id = userSession.userId, type = SessionConstants.CheckPinType.USER_ID.value)
            val needHash = checkPinHashV2UseCase(param).data.isNeedHash
            if(needHash) {
                validatePinV2(pin)
            } else {
                validatePin(pin)
            }
        }, onError = {
            onErrorValidatePin().invoke(it)
        })
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

    suspend fun getPublicKey(): KeyData {
        generatePublicKeyUseCase.setParams(SessionConstants.GenerateKeyModule.PIN_V2.value)
        val result = generatePublicKeyUseCase.executeOnBackground().keyData
        result.key = cleanPublicKey(result.key)
        return result
    }

    fun validatePinV2(pin: String) {
        launchCatchError(block = {
            val keyData = getPublicKey()
            val encryptedPin = RsaUtils.encryptWithSalt(pin, keyData.key, salt = OtpConstant.PIN_V2_SALT)
            hash = keyData.hash
            val param = ValidatePinV2Param(
                pin = encryptedPin,
                hash = hash
            )
            val result = validatePinV2UseCase(param).validatePinV2Data
            mutableValidatePinV2Response.value = Success(result)
        }, onError = {
            mutableValidatePinV2Response.value = Fail(it)
        })
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

    fun checkPinV2(pin: String) {
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
                it.data.is_success.toBoolean() -> {
                    saveToken(it.data)
                    mutableResetPin2FAResponse.value = Success(it.data)
                }
                it.data.error.isNotEmpty() ->
                    mutableResetPin2FAResponse.value = Fail(MessageErrorException(it.data.error))
                else -> mutableResetPin2FAResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun saveToken(data: ChangePin2FAData) {
        userSession.setToken(
            data.accessToken,
            "Bearer",
            EncoderDecoder.Encrypt(data.refreshToken, userSession.refreshTokenIV)
        )
    }

    fun resetPin(validateToken: String?) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_RESET_PIN]?.let { query ->
            val params = mapOf(
                ProfileCompletionQueryConstant.PARAM_VALIDATE_TOKEN to validateToken
            )
            resetPinUseCase.setTypeClass(ResetPinResponse::class.java)
            resetPinUseCase.setRequestParams(params)
            resetPinUseCase.setGraphqlQuery(query)
            resetPinUseCase.execute(
                onSuccessResetPin(),
                onErrorResetPin()
            )
        }
    }

    fun resetPinV2(validateToken: String) {
        launchCatchError(block = {
            val param = ResetPinV2Param(
                pinToken = pinPreference.getTempPin(),
                validateToken = validateToken
            )
            val result = resetPinV2UseCase(param).mutatePinV2data
            when {
                result.success -> mutableResetPinResponse.value = Success(result)
                result.errorAddChangePinData.isNotEmpty() -> mutableResetPinResponse.value = Fail(MessageErrorException(result.errorAddChangePinData[0].message))
                else -> mutableResetPinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            mutableResetPinResponse.value = Fail(it)
        })
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
                    mutableResetPinResponse.value =
                        Fail(MessageErrorException(it.data.errorAddChangePinData[0].message))
                else -> mutableResetPinResponse.value = Fail(RuntimeException())
            }
        }
    }

    suspend fun isNeedHash(id: String, type: String): Boolean {
        val param = PinStatusParam(id = id, type = type)
        return checkPinHashV2UseCase(param).data.isNeedHash
    }

    fun changePin(pin: String, pinConfirm: String, pinOld: String) {
        rawQueries[ProfileCompletionQueryConstant.MUTATION_UPDATE_PIN]?.let { query ->
            val params = mapOf(
                ProfileCompletionQueryConstant.PARAM_PIN to pin,
                ProfileCompletionQueryConstant.PARAM_PIN_CONFIRM to pinConfirm,
                ProfileCompletionQueryConstant.PARAM_PIN_OLD to pinOld
            )

            changePinUseCase.setTypeClass(ChangePinPojo::class.java)
            changePinUseCase.setRequestParams(params)
            changePinUseCase.setGraphqlQuery(query)
            changePinUseCase.execute(
                onSuccessChangePin(),
                onErrorChangePin()
            )
        }
    }

    fun changePinV2(pin: String, pinOld: String) {
        launchCatchError(block = {
            val keyData = getPublicKey()
            val encryptedPin = RsaUtils.encryptWithSalt(pin, keyData.key, salt = OtpConstant.PIN_V2_SALT)
            val encryptedOldPin = RsaUtils.encryptWithSalt(pinOld, keyData.key, salt = OtpConstant.PIN_V2_SALT)

            hash = keyData.hash

            val param = ChangePinV2Param(
                pin = encryptedPin,
                oldPin = encryptedOldPin,
                confirmPin = encryptedPin,
                hash = hash
            )
            val result = updatePinV2UseCase(param).mutatePinV2data

            when {
                result.success -> mutableChangePinResponse.value = Success(result)
                result.errorAddChangePinData.isNotEmpty() && result.errorAddChangePinData[0].message.isNotEmpty() -> {
                    mutableChangePinResponse.value = Fail(MessageErrorException(result.errorAddChangePinData[0].message))
                }
                else -> mutableChangePinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            mutableChangePinResponse.value = Fail(it)
        })
    }

    private fun onSuccessChangePin(): (ChangePinPojo) -> Unit {
        return {
            when {
                it.data.success -> mutableChangePinResponse.value = Success(it.data)
                it.data.errorAddChangePinData.isNotEmpty() && it.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableChangePinResponse.value =
                        Fail(MessageErrorException(it.data.errorAddChangePinData[0].message))
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

    companion object {
        private const val TYPE_SUCCESS = 1

        fun Int.toBoolean(): Boolean {
            return this == TYPE_SUCCESS
        }
    }
}