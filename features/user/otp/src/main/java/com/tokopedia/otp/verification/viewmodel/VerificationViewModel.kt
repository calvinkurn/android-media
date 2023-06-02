package com.tokopedia.otp.verification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.common.idling_resource.TkpdIdlingResource
import com.tokopedia.otp.verification.data.OtpConstant
import com.tokopedia.otp.verification.data.OtpConstant.DEFAULT_OTP_BEHAVIOR_ONE
import com.tokopedia.otp.verification.data.OtpConstant.DEFAULT_OTP_BEHAVIOR_THREE
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.SMS_AND_CHANGE_PN_FOOTER_TEXT
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.SPAN_USE_OTHER_METHODS
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.SPAN_USE_SMS_METHOD
import com.tokopedia.otp.verification.data.OtpConstant.StaticText.spanFactory
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.pojo.GetOtpModeListParam
import com.tokopedia.otp.verification.domain.pojo.GetVerificationMethodPhoneRegisterMandatoryParam
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpRequestPhoneRegisterMandatoryParam
import com.tokopedia.otp.verification.domain.pojo.OtpValidatePhoneRegisterMandatoryParam
import com.tokopedia.otp.verification.domain.usecase.GetOtpModeListUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodInactivePhoneUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodPhoneRegisterMandatoryUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase2FA
import com.tokopedia.otp.verification.domain.usecase.InactivePhoneVerificationMethodeParams
import com.tokopedia.otp.verification.domain.usecase.OtpValidatePhoneRegisterMandatoryUseCase
import com.tokopedia.otp.verification.domain.usecase.OtpValidateUseCase
import com.tokopedia.otp.verification.domain.usecase.OtpValidateUseCase2FA
import com.tokopedia.otp.verification.domain.usecase.SendOtp2FAUseCase
import com.tokopedia.otp.verification.domain.usecase.SendOtpPhoneRegisterMandatoryUseCase
import com.tokopedia.otp.verification.domain.usecase.SendOtpUseCase
import com.tokopedia.otp.verification.view.uimodel.DefaultOtpUiModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.pin.PinStatusParam
import com.tokopedia.sessioncommon.domain.usecase.CheckPinHashV2UseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

open class VerificationViewModel @Inject constructor(
    private val getVerificationMethodUseCase: GetVerificationMethodUseCase,
    private val getOtpModeListUseCase: GetOtpModeListUseCase,
    private val getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FA,
    private val getVerificationMethodInactivePhoneUseCase: GetVerificationMethodInactivePhoneUseCase,
    private val getVerificationMethodPhoneRegisterMandatoryUseCase: GetVerificationMethodPhoneRegisterMandatoryUseCase,
    private val checkPinHashV2UseCase: CheckPinHashV2UseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    private val otpValidateUseCase: OtpValidateUseCase,
    private val otpValidateUseCase2FA: OtpValidateUseCase2FA,
    private val otpValidatePhoneRegisterMandatoryUseCase: OtpValidatePhoneRegisterMandatoryUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
    private val sendOtpUseCase2FA: SendOtp2FAUseCase,
    private val sendOtpPhoneRegisterMandatoryUseCase: SendOtpPhoneRegisterMandatoryUseCase,
    private val userSession: UserSessionInterface,
    private val remoteConfig: RemoteConfig,
    dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    private val _getVerificationMethodResult = MutableLiveData<Result<OtpModeListData>>()
    val getVerificationMethodResult: LiveData<Result<OtpModeListData>>
        get() = _getVerificationMethodResult

    private val _sendOtpResult = MutableLiveData<Result<OtpRequestData>>()
    val sendOtpResult: LiveData<Result<OtpRequestData>>
        get() = _sendOtpResult

    private val _otpValidateResult = MutableLiveData<Result<OtpValidateData>>()
    val otpValidateResult: LiveData<Result<OtpValidateData>>
        get() = _otpValidateResult

    private val _defaultOtpUiModel = MutableLiveData<DefaultOtpUiModel>()
    val defaultOtpUiModel: LiveData<DefaultOtpUiModel>
        get() = _defaultOtpUiModel

    private val _gotoInputOtp = SingleLiveEvent<ModeListData>()
    val gotoInputOtp: LiveData<ModeListData> = _gotoInputOtp

    var done = false
    var isLoginRegisterFlow = false

    fun getVerificationMethodPhoneRegisterMandatory(
        otpType: String,
        validateToken: String,
        email: String,
        msisdn: String
    ) {
        val param = GetVerificationMethodPhoneRegisterMandatoryParam(
            otpType = otpType,
            msisdn = msisdn,
            email = email,
            validateToken = validateToken
        )
        launchCatchError(block = {
            val response = getVerificationMethodPhoneRegisterMandatoryUseCase(param)

            when {
                response.data.success -> {
                    _getVerificationMethodResult.value = Success(response.data)
                }
                response.data.errorMessage.isNotEmpty() -> {
                    _getVerificationMethodResult.value =
                        Fail(MessageErrorException(response.data.errorMessage))
                }
                else -> {
                    _getVerificationMethodResult.value = Fail(Throwable())
                }
            }
        }, onError = {
                _getVerificationMethodResult.value = Fail(it)
            })
    }

    fun isSmsHidden(defaultBehaviorMode: Int): Boolean {
        return defaultBehaviorMode == DEFAULT_OTP_BEHAVIOR_ONE || defaultBehaviorMode == DEFAULT_OTP_BEHAVIOR_THREE
    }

    fun renderInitialDefaultOtp(otpModeListData: OtpModeListData) {
        val newItems = ArrayList(otpModeListData.modeList)
        val smsOtp = otpModeListData.modeList.find { it.modeText == OtpConstant.OtpMode.SMS }
        var footerSpan = ""
        var footerText = ""
        var action = { }

        if (otpModeListData.modeList.find { it.modeCode == otpModeListData.defaultMode } != null) {
            newItems.removeAll { it.modeCode != otpModeListData.defaultMode }
        }

        /**
         * if defaultMode code is not exists on the otp mode list
         * make it as default method is off
         */
        if (isSmsHidden(otpModeListData.defaultBehaviorMode) && newItems.size != 1) {
            newItems.removeAll {
                it.modeText == OtpConstant.OtpMode.SMS
            }
            if (smsOtp != null) {
                footerSpan = SPAN_USE_SMS_METHOD
                footerText = spanFactory(SPAN_USE_SMS_METHOD, otpModeListData.linkType)
                action = { goToInputOtp(smsOtp) }
            }
        }

        /**
         * if otp mode list only has 2 options that contains sms but the behaviour mode is hide sms
         * then use [SMS_AND_CHANGE_PN_FOOTER_TEXT] instead for footerText.
         */
        else if (otpModeListData.modeList.size == 2 && otpModeListData.modeList.contains(smsOtp) &&
            isSmsHidden(otpModeListData.defaultBehaviorMode)
        ) {
            if (smsOtp != null) {
                footerSpan = SPAN_USE_SMS_METHOD
                footerText = spanFactory(SPAN_USE_SMS_METHOD, otpModeListData.linkType)
                action = { goToInputOtp(smsOtp) }
            }

            /* if newItems has less size than original mode list then shows CTA 'Gunakan metode lain' */
        } else if (otpModeListData.modeList.size > 1 && newItems.size < otpModeListData.modeList.size) {
            footerSpan = SPAN_USE_OTHER_METHODS
            footerText = spanFactory(SPAN_USE_OTHER_METHODS, otpModeListData.linkType)
            action = { showAllMethod() }
        }

        _defaultOtpUiModel.value = DefaultOtpUiModel(
            footerText,
            footerSpan,
            otpModeListData.linkType,
            action,
            displayedModeList = newItems,
            originalOtpModeList = otpModeListData.modeList,
            defaultMode = otpModeListData.defaultMode,
            defaultBehaviorMode = otpModeListData.defaultBehaviorMode
        )
    }

    fun renderInitialDefaultOtpOff(otpModeListData: OtpModeListData) {
        val newItems = ArrayList(otpModeListData.modeList)
        val smsOtp = otpModeListData.modeList.find { it.modeText == OtpConstant.OtpMode.SMS }
        var footerSpan = ""
        var footerText = ""
        var action = {}
        if (smsOtp != null && otpModeListData.modeList.size > 1 && isSmsHidden(otpModeListData.defaultBehaviorMode)) {
            newItems.removeAll { it.modeText == OtpConstant.OtpMode.SMS }
            footerSpan = SPAN_USE_SMS_METHOD
            footerText = spanFactory(SPAN_USE_SMS_METHOD, otpModeListData.linkType)
            action = { goToInputOtp(smsOtp) }
        }

        _defaultOtpUiModel.value = DefaultOtpUiModel(
            footerText,
            footerSpan,
            otpModeListData.linkType,
            action,
            displayedModeList = newItems,
            originalOtpModeList = otpModeListData.modeList,
            defaultMode = otpModeListData.defaultMode,
            defaultBehaviorMode = otpModeListData.defaultBehaviorMode
        )
    }

    fun getVerificationMethod2FA(
        otpType: String,
        validateToken: String,
        userIdEnc: String
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            val params =
                getVerificationMethodUseCase2FA.getParams2FA(otpType, validateToken, userIdEnc)
            val data = getVerificationMethodUseCase2FA.getData(params).data
            when {
                data.success -> {
                    _getVerificationMethodResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _getVerificationMethodResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _getVerificationMethodResult.postValue(Fail(Throwable()))
                    TkpdIdlingResource.decrement()
                }
            }
        }, {
            _getVerificationMethodResult.postValue(Fail(it))
            TkpdIdlingResource.decrement()
        })
    }

    fun getOtpModeListForDefaultOtp(
        otpType: String,
        userId: String,
        msisdn: String,
        email: String,
        timeUnix: String,
        authenticity: String,
        cache: DefaultOtpUiModel? = null
    ) {
        launch {
            try {
                if (cache != null) {
                    showAllMethod(cache)
                } else {
                    val param = GetOtpModeListParam(otpType, userId, msisdn, email, timeUnix, authenticity)
                    val result = getOtpModeListUseCase(param)
                    if (result.defaultBehaviorMode > DEFAULT_OTP_BEHAVIOR_ONE && result.defaultMode > 0) {
                        renderInitialDefaultOtp(result)
                    } else if (result.defaultBehaviorMode == DEFAULT_OTP_BEHAVIOR_ONE) {
                        renderInitialDefaultOtpOff(result)
                    } else {
                        _getVerificationMethodResult.value = Success(result)
                    }
                }
            } catch (e: Throwable) {
                _getVerificationMethodResult.value = Fail(e)
            }
        }
    }

    fun showAllMethod(cache: DefaultOtpUiModel? = null) {
        val previousData = cache ?: _defaultOtpUiModel.value
        if (previousData != null) {
            val newItems = ArrayList(previousData.originalOtpModeList)
            val smsOtp = previousData.originalOtpModeList.find { it.modeText == OtpConstant.OtpMode.SMS }
            var footerSpan = ""
            var footerText = ""
            var action = { }

            if (smsOtp != null) {
                if (isSmsHidden(previousData.defaultBehaviorMode)) {
                    newItems.removeAll { it.modeText == OtpConstant.OtpMode.SMS }
                    footerSpan = SPAN_USE_SMS_METHOD
                    footerText = spanFactory(SPAN_USE_SMS_METHOD, previousData.linkType)
                    action = { goToInputOtp(smsOtp) }
                }
            }

            _defaultOtpUiModel.value = DefaultOtpUiModel(
                footerText,
                footerSpan,
                previousData.linkType,
                action,
                displayedModeList = newItems,
                originalOtpModeList = previousData.originalOtpModeList,
                defaultMode = previousData.defaultMode,
                defaultBehaviorMode = previousData.defaultBehaviorMode
            )
        }
    }

    fun goToInputOtp(otpData: ModeListData) {
        _gotoInputOtp.value = otpData
        done = true
    }

    fun getVerificationMethod(
        otpType: String,
        userId: String,
        msisdn: String,
        email: String,
        timeUnix: String,
        authenticity: String
    ) {
        launchCatchError(block = {
            TkpdIdlingResource.increment()
            val params = getVerificationMethodUseCase.getParams(
                otpType,
                userId,
                msisdn,
                email,
                timeUnix,
                authenticity
            )
            val data = getVerificationMethodUseCase.getData(params).data
            when {
                data.success -> {
                    _getVerificationMethodResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _getVerificationMethodResult.value =
                        Fail(MessageErrorException(data.errorMessage))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _getVerificationMethodResult.value = Fail(Throwable())
                    TkpdIdlingResource.decrement()
                }
            }
        }, onError = {
                _getVerificationMethodResult.value = Fail(it)
                TkpdIdlingResource.decrement()
            })
    }

    fun getVerificationMethodInactive(
        otpType: String,
        userId: String = "",
        msisdn: String = "",
        email: String = "",
        validateToken: String = "",
        userIdEnc: String = ""
    ) {
        launchCatchError(block = {
            TkpdIdlingResource.increment()
            val response = getVerificationMethodInactivePhoneUseCase(
                InactivePhoneVerificationMethodeParams(
                    otpType = otpType,
                    userId = userId,
                    msisdn = msisdn,
                    email = email,
                    validateToken = validateToken,
                    userIDEnc = userIdEnc
                )
            )

            when {
                response.data.success -> {
                    _getVerificationMethodResult.value = Success(response.data)
                    TkpdIdlingResource.decrement()
                }
                response.data.errorMessage.isNotEmpty() -> {
                    _getVerificationMethodResult.value =
                        Fail(MessageErrorException(response.data.errorMessage))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _getVerificationMethodResult.value = Fail(Throwable())
                    TkpdIdlingResource.decrement()
                }
            }
        }, onError = {
                _getVerificationMethodResult.value = Fail(it)
                TkpdIdlingResource.decrement()
            })
    }

    fun sendOtpPhoneRegisterMandatory(
        otpType: String,
        mode: String,
        msisdn: String,
        email: String,
        otpDigit: Int,
        validateToken: String
    ) {
        val param = OtpRequestPhoneRegisterMandatoryParam(
            otpType = otpType,
            mode = mode,
            msisdn = msisdn,
            email = email,
            otpDigit = otpDigit,
            validateToken = validateToken
        )
        launchCatchError(coroutineContext, {
            val response = sendOtpPhoneRegisterMandatoryUseCase(param)
            val data = response.data
            _sendOtpResult.value = Success(data)
        }, {
            _sendOtpResult.postValue(Fail(it))
        })
    }

    fun sendOtp2FA(
        otpType: String,
        mode: String,
        msisdn: String,
        email: String,
        otpDigit: Int,
        validateToken: String,
        userIdEnc: String
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            val params = sendOtpUseCase2FA.getParams(
                otpType,
                mode,
                msisdn,
                email,
                otpDigit,
                userIdEnc = userIdEnc,
                validateToken = validateToken
            )
            val data = sendOtpUseCase2FA.getData(params).data
            _sendOtpResult.value = Success(data)
            TkpdIdlingResource.decrement()
        }, {
            _sendOtpResult.postValue(Fail(it))
            TkpdIdlingResource.decrement()
        })
    }

    fun sendOtp(
        otpType: String,
        mode: String,
        msisdn: String,
        email: String,
        otpDigit: Int
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            val params = sendOtpUseCase.getParams(otpType, mode, msisdn, email, otpDigit)
            val data = sendOtpUseCase.getData(params).data
            _sendOtpResult.value = Success(data)
            TkpdIdlingResource.decrement()
        }, {
            _sendOtpResult.postValue(Fail(it))
            TkpdIdlingResource.decrement()
        })
    }

    fun otpValidatePhoneRegisterMandatory(
        code: String,
        otpType: String,
        mode: String,
        msisdn: String,
        email: String,
        validateToken: String
    ) {
        val params = OtpValidatePhoneRegisterMandatoryParam(
            code,
            otpType,
            mode,
            msisdn,
            email,
            validateToken
        )
        launchCatchError(coroutineContext, {
            val data = otpValidatePhoneRegisterMandatoryUseCase(params).data

            when {
                data.success -> {
                    _otpValidateResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _otpValidateResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _otpValidateResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _otpValidateResult.postValue(Fail(it))
        })
    }

    fun otpValidate2FA(
        otpType: String,
        validateToken: String,
        userIdEnc: String,
        mode: String,
        code: String,
        msisdn: String = "",
        userId: Int
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            var params = otpValidateUseCase2FA.getParams(
                otpType = otpType,
                validateToken = validateToken,
                userIdEnc = userIdEnc,
                mode = mode,
                code = code,
                msisdn = msisdn
            )

            if (mode == OtpConstant.OtpMode.PIN &&
                isNeedHash(createCheckPinV2Param(userId.toString(), msisdn, ""))
            ) {
                val keyData = getPublicKey()
                val encryptedPin = RsaUtils.encryptWithSalt(
                    code,
                    keyData.key,
                    salt = OtpConstant.PIN_V2_SALT
                )
                params = combineWithV2param(
                    params,
                    hashedPin = encryptedPin,
                    usePinHash = true,
                    hash = keyData.hash
                )
            }

            val data = otpValidateUseCase2FA.getData(params).data
            when {
                data.success -> {
                    _otpValidateResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _otpValidateResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _otpValidateResult.postValue(Fail(Throwable()))
                    TkpdIdlingResource.decrement()
                }
            }
        }, {
            _otpValidateResult.postValue(Fail(it))
            TkpdIdlingResource.decrement()
        })
    }

    fun otpValidate(
        code: String,
        otpType: String,
        msisdn: String,
        fpData: String,
        getSL: String,
        email: String,
        mode: String,
        signature: String,
        timeUnix: String,
        userId: Int
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            var params = otpValidateUseCase.getParams(
                code,
                otpType,
                msisdn,
                fpData,
                getSL,
                email,
                mode,
                signature,
                timeUnix,
                userId
            )
            if (mode == OtpConstant.OtpMode.PIN && isNeedHash(createCheckPinV2Param(userId.toString(), msisdn, email))) {
                val keyData = getPublicKey()
                val encryptedPin = RsaUtils.encryptWithSalt(
                    code,
                    keyData.key,
                    salt = OtpConstant.PIN_V2_SALT
                )
                params = combineWithV2param(
                    params,
                    hashedPin = encryptedPin,
                    usePinHash = true,
                    hash = keyData.hash
                )
            }
            val data = otpValidateUseCase.getData(params).data
            when {
                data.success -> {
                    _otpValidateResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _otpValidateResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _otpValidateResult.postValue(Fail(Throwable()))
                    TkpdIdlingResource.decrement()
                }
            }
        }, {
            _otpValidateResult.postValue(Fail(it))
            TkpdIdlingResource.decrement()
        })
    }

    fun combineWithV2param(
        oldParam: Map<String, Any>,
        hashedPin: String,
        usePinHash: Boolean,
        hash: String
    ): Map<String, Any> {
        val newMap: MutableMap<String, Any> = mutableMapOf()
        newMap.putAll(oldParam)
        newMap[OtpValidateUseCase.PARAM_CODE] = ""
        newMap[OtpValidateUseCase.PARAM_PIN] = hashedPin
        newMap[OtpValidateUseCase.PARAM_PIN_HASH] = hash
        newMap[OtpValidateUseCase.PARAM_USE_PIN_HASH] = usePinHash
        return newMap
    }

    fun createCheckPinV2Param(userId: String, msisdn: String, email: String): PinStatusParam {
        var id = ""
        var type = ""
        if (userId.isNotEmpty() && userId != "0") {
            id = userId
            type = SessionConstants.CheckPinType.USER_ID.value
        } else if (msisdn.isNotEmpty()) {
            id = msisdn
            type = SessionConstants.CheckPinType.PHONE.value
        } else if (email.isNotEmpty()) {
            id = email
            type = SessionConstants.CheckPinType.EMAIL.value
        }
        return PinStatusParam(id = id, type = type)
    }

    suspend fun isNeedHash(param: PinStatusParam): Boolean {
        return checkPinHashV2UseCase(param).data.isNeedHash
    }

    suspend fun getPublicKey(): KeyData {
        val result = generatePublicKeyUseCase().keyData
        result.key = SessionConstants.cleanPublicKey(result.key)
        return result
    }

    public override fun onCleared() {
        val clear = remoteConfig.getBoolean(RemoteConfigKey.PRE_OTP_LOGIN_CLEAR, true)
        if (clear) {
            // if user interrupted login / register otp flow (not done), delete the token
            if (!done && isLoginRegisterFlow) {
                userSession.setToken(null, null, null)
                ServerLogger.log(
                    Priority.P2,
                    "BUYER_FLOW_LOGIN",
                    mapOf("type" to "token_cleared_during_verification")
                )
            }
        }
        super.onCleared()
    }
}
