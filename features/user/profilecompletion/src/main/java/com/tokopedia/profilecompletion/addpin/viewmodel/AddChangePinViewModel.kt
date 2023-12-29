package com.tokopedia.profilecompletion.addpin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.CheckPinData
import com.tokopedia.profilecompletion.addpin.data.CreatePinV2Param
import com.tokopedia.profilecompletion.addpin.data.SkipOtpPinData
import com.tokopedia.profilecompletion.addpin.data.StatusPinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinData
import com.tokopedia.profilecompletion.addpin.data.usecase.CreatePinV2UseCase
import com.tokopedia.profilecompletion.changepin.data.model.MutatePinV2Data
import com.tokopedia.profilecompletion.common.PinPreference
import com.tokopedia.profilecompletion.common.model.CheckPinV2Data
import com.tokopedia.profilecompletion.common.model.CheckPinV2Param
import com.tokopedia.profilecompletion.common.usecase.CheckPinV2UseCase
import com.tokopedia.profilecompletion.data.SkipOtpPinParam
import com.tokopedia.profilecompletion.domain.CheckPinUseCase
import com.tokopedia.profilecompletion.domain.CreatePinUseCase
import com.tokopedia.profilecompletion.domain.SkipOtpPinUseCase
import com.tokopedia.profilecompletion.domain.StatusPinUseCase
import com.tokopedia.profilecompletion.domain.ValidatePinUseCase
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
    private val createPinUseCase: CreatePinUseCase,
    private val createPinV2UseCase: CreatePinV2UseCase,
    private val checkPinUseCase: CheckPinUseCase,
    private val checkPinV2UseCase: CheckPinV2UseCase,
    private val statusPinUseCase: StatusPinUseCase,
    private val validatePinUseCase: ValidatePinUseCase,
    private val skipOtpPinUseCase: SkipOtpPinUseCase,
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
        launchCatchError(block = {
            val response = createPinUseCase(token)

            when {
                response.data.success -> mutableAddPinResponse.value = Success(response.data)
                response.data.errorAddChangePinData.isNotEmpty() && response.data.errorAddChangePinData[0].message.isNotEmpty() ->
                    mutableAddPinResponse.value =
                        Fail(MessageErrorException(response.data.errorAddChangePinData[0].message))
                else -> mutableAddPinResponse.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableAddPinResponse.value = Fail(it)
            })
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
                result.errors.isNotEmpty() ->
                    mutableMutatePin.value =
                        Fail(MessageErrorException(result.errors[0].message))
                else -> mutableMutatePin.value = Fail(RuntimeException())
            }
        }, onError = {
                mutableMutatePin.value = Fail(it)
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
        pinPreference.clearTempPin()
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

    fun getStatusPin() {
        loadingState.value = true

        launchCatchError(block = {
            val response = statusPinUseCase(Unit)

            loadingState.value = false
            if (response.data.errorMessage.isNotEmpty()) {
                mutableGetStatusPinResponse.value =
                    Fail(MessageErrorException(response.data.errorMessage))
            } else {
                mutableGetStatusPinResponse.value = Success(response.data)
            }
        }, onError = {
                loadingState.value = false
                mutableGetStatusPinResponse.value = Fail(it)
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

    fun checkSkipOtpPin(validateToken: String) {
        val params = SkipOtpPinParam(
            otpType = OTP_TYPE_SKIP_VALIDATION,
            validateToken = validateToken
        )

        launchCatchError(block = {
            val response = skipOtpPinUseCase(params)

            when {
                response.data.errorMessage.isNotEmpty() ->
                    mutableSkipOtpPinResponse.value =
                        Fail(MessageErrorException(response.data.errorMessage))
                else -> mutableSkipOtpPinResponse.value = Success(response.data)
            }
        }, onError = {
                mutableSkipOtpPinResponse.value = Fail(it)
            })
    }

    suspend fun getPublicKey(): KeyData {
        return generatePublicKeyUseCase(SessionConstants.GenerateKeyModule.PIN_V2.value).keyData
    }

    companion object {
        const val OTP_TYPE_SKIP_VALIDATION = 124
    }
}
