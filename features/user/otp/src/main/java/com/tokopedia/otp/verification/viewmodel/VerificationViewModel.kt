package com.tokopedia.otp.verification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.verification.common.DispatcherProvider
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpModeListData
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

class VerificationViewModel @Inject constructor(
        private val getVerificationMethodUseCase: GetVerificationMethodUseCase,
        private val getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FA,
        private val otpValidateUseCase: OtpValidateUseCase,
        private val otpValidateUseCase2FA: OtpValidateUseCase2FA,
        private val sendOtpUseCase: SendOtpUseCase,
        private val sendOtpUseCase2FA: SendOtp2FAUseCase,
        dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider.ui()) {

    private val _getVerificationMethodResult = MutableLiveData<Result<OtpModeListData>>()
    val getVerificationMethodResult: LiveData<Result<OtpModeListData>>
        get() = _getVerificationMethodResult

    private val _sendOtpResult = MutableLiveData<Result<OtpRequestData>>()
    val sendOtpResult: LiveData<Result<OtpRequestData>>
        get() = _sendOtpResult

    private val _otpValidateResult = MutableLiveData<Result<OtpValidateData>>()
    val otpValidateResult: LiveData<Result<OtpValidateData>>
        get() = _otpValidateResult


    private fun getVerificationMethod2FA(
            otpType: String,
            ValidateToken: String,
            UserIDEnc: String
    ) {
        launchCatchError(coroutineContext, {
            val params = getVerificationMethodUseCase2FA.getParams2FA(otpType, ValidateToken, UserIDEnc)

            val data = getVerificationMethodUseCase2FA.getData(params).data
            when {
                data.success -> {
                    _getVerificationMethodResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _getVerificationMethodResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _getVerificationMethodResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _getVerificationMethodResult.postValue(Fail(it))
        })
    }

    @JvmOverloads
    fun getVerificationMethod(
            otpType: String,
            userId: String,
            msisdn: String = "",
            email: String = "",
            userIdEnc: String = "",
            accessToken: String = ""
    ) {
        if((otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE.toString() || otpType == OtpConstant.OtpType.RESET_PIN.toString()) && userIdEnc.isNotEmpty()){
            getVerificationMethod2FA(otpType, accessToken, userIdEnc)
        }else {
            launchCatchError(coroutineContext, {
                val params = getVerificationMethodUseCase.getParams(otpType, userId, msisdn, email)

                val data = getVerificationMethodUseCase.getData(params).data
                when {
                    data.success -> {
                        _getVerificationMethodResult.value = Success(data)
                    }
                    data.errorMessage.isNotEmpty() -> {
                        _getVerificationMethodResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    }
                    else -> {
                        _getVerificationMethodResult.postValue(Fail(Throwable()))
                    }
                }
            }, {
                _getVerificationMethodResult.postValue(Fail(it))
            })
        }
    }

    @JvmOverloads
    fun sendOtp2FA(
            otpType: String,
            mode: String,
            msisdn: String = "",
            email: String = "",
            otpDigit: Int = 6,
            validateToken: String,
            userIdEnc: String
    ) {
        launchCatchError(coroutineContext, {
            val params = sendOtpUseCase2FA.getParams(otpType, mode, msisdn, email, otpDigit, userIdEnc = userIdEnc, validateToken = validateToken)
            val data = sendOtpUseCase2FA.getData(params).data
            when {
                data.success -> {
                    _sendOtpResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _sendOtpResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _sendOtpResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _sendOtpResult.postValue(Fail(it))
        })
    }

    @JvmOverloads
    fun sendOtp(
            otpType: String,
            mode: String,
            msisdn: String = "",
            email: String = "",
            otpDigit: Int = 6,
            validateToken: String = "",
            userIdEnc: String = ""
    ) {
        if(validateToken.isNotEmpty() && userIdEnc.isNotEmpty()){
            sendOtp2FA(otpType, mode, msisdn, email, otpDigit, validateToken, userIdEnc)
        }else {
            launchCatchError(coroutineContext, {
                val params = sendOtpUseCase.getParams(otpType, mode, msisdn, email, otpDigit)
                val data = sendOtpUseCase.getData(params).data
                when {
                    data.success -> {
                        _sendOtpResult.value = Success(data)
                    }
                    data.errorMessage.isNotEmpty() -> {
                        _sendOtpResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    }
                    else -> {
                        _sendOtpResult.postValue(Fail(Throwable()))
                    }
                }
            }, {
                _sendOtpResult.postValue(Fail(it))
            })
        }
    }

    private fun otpValidate2FA(
            otpType: String,
            validateToken: String,
            userIdEnc: String,
            mode: String = "",
            code: String
    ) {
        launchCatchError(coroutineContext, {
            val params = otpValidateUseCase2FA.getParams(otpType = otpType, validateToken = validateToken, userIdEnc = userIdEnc, mode = mode, code = code)
            val data = otpValidateUseCase2FA.getData(params).data
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

    @JvmOverloads
    fun otpValidate(
            code: String,
            otpType: String,
            msisdn: String = "",
            fpData: String = "",
            getSL: String = "",
            email: String = "",
            mode: String = "",
            signature: String = "",
            timeUnix: String = "",
            userId: Int,
            validateToken: String = "",
            userIdEnc: String = ""
    ) {
        if((otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE.toString() || otpType == OtpConstant.OtpType.RESET_PIN.toString()) && userIdEnc.isNotEmpty()){
            otpValidate2FA(otpType, validateToken, userIdEnc, mode, code)
        }else {
            launchCatchError(coroutineContext, {
                val params = otpValidateUseCase.getParams(code, otpType, msisdn, fpData, getSL, email, mode, signature, timeUnix, userId)
                val data = otpValidateUseCase.getData(params).data
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
    }
}