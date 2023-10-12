package com.tokopedia.profilecompletion.changepin.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.CheckPinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinData
import com.tokopedia.profilecompletion.changepin.data.ChangePin2FAData
import com.tokopedia.profilecompletion.changepin.data.model.ChangePinV2Param
import com.tokopedia.profilecompletion.changepin.data.model.ResetPinV2Param
import com.tokopedia.profilecompletion.changepin.data.usecase.ResetPinV2UseCase
import com.tokopedia.profilecompletion.changepin.data.usecase.UpdatePinV2UseCase
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Param
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.data.ChangePinParam
import com.tokopedia.profilecompletion.data.CheckPin2FAParam
import com.tokopedia.profilecompletion.data.ResetPin2FAParam
import com.tokopedia.profilecompletion.domain.ChangePinUseCase
import com.tokopedia.profilecompletion.domain.CheckPin2FaUseCase
import com.tokopedia.profilecompletion.domain.CheckPinUseCase
import com.tokopedia.profilecompletion.domain.ResetPin2FaUseCase
import com.tokopedia.profilecompletion.domain.ResetPinUseCase
import com.tokopedia.profilecompletion.domain.ValidatePinUseCase
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
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 23/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangePinViewModel @Inject constructor(
    private val validatePinUseCase: ValidatePinUseCase,
    private val validatePinV2UseCase: ValidatePinV2UseCase,
    private val checkPinUseCase: CheckPinUseCase,
    private val checkPin2FaUseCase: CheckPin2FaUseCase,
    private val resetPinUseCase: ResetPinUseCase,
    private val resetPinV2UseCase: ResetPinV2UseCase,
    private val resetPin2FaUseCase: ResetPin2FaUseCase,
    private val changePinUseCase: ChangePinUseCase,
    private val updatePinV2UseCase: UpdatePinV2UseCase,
    private val checkPinV2UseCase: CheckPinV2UseCase,
    private val userSession: UserSessionInterface,
    private val checkPinHashV2UseCase: CheckPinHashV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val pinPreference: PinPreference,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

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
            val param = PinStatusParam(
                id = userSession.userId,
                type = SessionConstants.CheckPinType.USER_ID.value
            )
            val needHash = checkPinHashV2UseCase(param).data.isNeedHash
            if (needHash) {
                validatePinV2(pin)
            } else {
                validatePin(pin)
            }
        }, onError = {
                mutableValidatePinResponse.value = Fail(it)
            })
    }

    fun validatePin(pin: String) {
        launchCatchError(block = {
            val response = validatePinUseCase(pin)

            when {
                response.data.valid -> mutableValidatePinResponse.value = Success(response.data)
                response.data.errorMessage.isNotEmpty() ->
                    mutableValidatePinResponse.value = Success(response.data)
                else -> mutableValidatePinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableValidatePinResponse.value = Fail(it)
            })
    }

    suspend fun getPublicKey(): KeyData {
        val result = generatePublicKeyUseCase(SessionConstants.GenerateKeyModule.PIN_V2.value).keyData
        result.key = cleanPublicKey(result.key)
        return result
    }

    fun validatePinV2(pin: String) {
        launchCatchError(block = {
            val keyData = getPublicKey()
            val encryptedPin =
                RsaUtils.encryptWithSalt(pin, keyData.key, salt = OtpConstant.PIN_V2_SALT)
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

    fun checkPin2FA(pin: String, validateToken: String, userId: String) {
        val params = CheckPin2FAParam(
            pin = pin,
            validateToken = validateToken,
            action = "reset",
            userId = userId.toIntOrZero()
        )

        launchCatchError(block = {
            val response = checkPin2FaUseCase(params)

            when {
                response.data.valid -> mutableCheckPinResponse.value = Success(response.data)
                response.data.errorMessage.isNotEmpty() ->
                    mutableCheckPinResponse.value = Success(response.data)
                else -> mutableCheckPinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableCheckPinResponse.value = Fail(it)
            })
    }

    fun checkPin(pin: String) {
        launchCatchError(block = {
            val response = checkPinUseCase(pin)

            when {
                response.data.valid -> mutableCheckPinResponse.value = Success(response.data)
                response.data.errorMessage.isNotEmpty() ->
                    mutableCheckPinResponse.value = Success(response.data)
                else -> mutableCheckPinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableCheckPinResponse.value = Fail(it)
            })
    }

    fun checkPinV2(pin: String) {
        launchCatchError(block = {
            val keyData = getPublicKey()
            val encryptedPin =
                RsaUtils.encryptWithSalt(pin, keyData.key, salt = OtpConstant.PIN_V2_SALT)
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

    fun resetPin2FA(userId: String, validateToken: String) {
        val params = ResetPin2FAParam(
            userId = userId.toIntOrZero(),
            validateToken = validateToken,
            grantType = "extension"
        )
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")

        launchCatchError(block = {
            val response = resetPin2FaUseCase(params)

            when {
                response.data.is_success.toBoolean() -> {
                    saveToken(response.data)
                    mutableResetPin2FAResponse.value = Success(response.data)
                }
                response.data.error.isNotEmpty() ->
                    mutableResetPin2FAResponse.value =
                        Fail(MessageErrorException(response.data.error))
                else -> mutableResetPin2FAResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableResetPin2FAResponse.value = Fail(it)
            })
    }

    private fun saveToken(data: ChangePin2FAData) {
        userSession.setToken(
            data.accessToken,
            "Bearer",
            EncoderDecoder.Encrypt(data.refreshToken, userSession.refreshTokenIV)
        )
    }

    fun resetPin(validateToken: String) {
        launchCatchError(block = {
            val response = resetPinUseCase(validateToken)

            when {
                response.data.success -> mutableResetPinResponse.value = Success(response.data)
                response.data.errorAddChangePinData.isNotEmpty() && response.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableResetPinResponse.value =
                        Fail(MessageErrorException(response.data.errorAddChangePinData[0].message))
                else -> mutableResetPinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableResetPinResponse.value = Fail(it)
            })
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
                result.errorAddChangePinData.isNotEmpty() ->
                    mutableResetPinResponse.value =
                        Fail(MessageErrorException(result.errorAddChangePinData[0].message))
                else -> mutableResetPinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableResetPinResponse.value = Fail(it)
            })
    }

    suspend fun isNeedHash(id: String, type: String): Boolean {
        val param = PinStatusParam(id = id, type = type)
        return checkPinHashV2UseCase(param).data.isNeedHash
    }

    fun changePin(pin: String, pinConfirm: String, pinOld: String) {
        val params = ChangePinParam(
            pin = pin,
            pinConfirm = pinConfirm,
            pinOld = pinOld
        )

        launchCatchError(block = {
            val response = changePinUseCase(params)

            when {
                response.data.success -> mutableChangePinResponse.value = Success(response.data)
                response.data.errorAddChangePinData.isNotEmpty() && response.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableChangePinResponse.value =
                        Fail(MessageErrorException(response.data.errorAddChangePinData[0].message))
                else -> mutableChangePinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableChangePinResponse.value = Fail(it)
            })
    }

    fun changePinV2(pin: String, pinOld: String) {
        launchCatchError(block = {
            val keyData = getPublicKey()
            val encryptedPin =
                RsaUtils.encryptWithSalt(pin, keyData.key, salt = OtpConstant.PIN_V2_SALT)
            val encryptedOldPin =
                RsaUtils.encryptWithSalt(pinOld, keyData.key, salt = OtpConstant.PIN_V2_SALT)

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
                    mutableChangePinResponse.value =
                        Fail(MessageErrorException(result.errorAddChangePinData[0].message))
                }
                else -> mutableChangePinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableChangePinResponse.value = Fail(it)
            })
    }

    companion object {
        private const val TYPE_SUCCESS = 1

        fun Int.toBoolean(): Boolean {
            return this == TYPE_SUCCESS
        }
    }
}
