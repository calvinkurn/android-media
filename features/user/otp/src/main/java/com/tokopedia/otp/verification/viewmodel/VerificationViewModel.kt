package com.tokopedia.otp.verification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.common.idling_resource.TkpdIdlingResource
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

open class VerificationViewModel @Inject constructor(
        private val getVerificationMethodUseCase: GetVerificationMethodUseCase,
        private val getVerificationMethodUseCase2FA: GetVerificationMethodUseCase2FA,
        private val otpValidateUseCase: OtpValidateUseCase,
        private val otpValidateUseCase2FA: OtpValidateUseCase2FA,
        private val sendOtpUseCase: SendOtpUseCase,
        private val sendOtpUseCase2FA: SendOtp2FAUseCase,
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

    private val _onClearedViewModel = MutableLiveData<Unit>()
    val onClearedViewModel: LiveData<Unit>
        get() = _onClearedViewModel

    fun getVerificationMethod2FA(
            otpType: String,
            validateToken: String,
            userIdEnc: String
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            val params = getVerificationMethodUseCase2FA.getParams2FA(otpType, validateToken, userIdEnc)
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

    fun getVerificationMethod(
            otpType: String,
            userId: String,
            msisdn: String,
            email: String
    ) {
        launchCatchError(block = {
            TkpdIdlingResource.increment()
            val params = getVerificationMethodUseCase.getParams(otpType, userId, msisdn, email)
            val data = getVerificationMethodUseCase.getData(params).data
            when {
                data.success -> {
                    _getVerificationMethodResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _getVerificationMethodResult.value = Fail(MessageErrorException(data.errorMessage))
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
            val params = sendOtpUseCase2FA.getParams(otpType, mode, msisdn, email, otpDigit, userIdEnc = userIdEnc, validateToken = validateToken)
            val data = sendOtpUseCase2FA.getData(params).data
            when {
                data.success -> {
                    _sendOtpResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _sendOtpResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _sendOtpResult.postValue(Fail(Throwable()))
                    TkpdIdlingResource.decrement()
                }
            }
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
            when {
                data.success -> {
                    _sendOtpResult.value = Success(data)
                    TkpdIdlingResource.decrement()
                }
                data.errorMessage.isNotEmpty() -> {
                    _sendOtpResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                    TkpdIdlingResource.decrement()
                }
                else -> {
                    _sendOtpResult.postValue(Fail(Throwable()))
                    TkpdIdlingResource.decrement()
                }
            }
        }, {
            _sendOtpResult.postValue(Fail(it))
            TkpdIdlingResource.decrement()
        })
    }

    fun otpValidate2FA(
            otpType: String,
            validateToken: String,
            userIdEnc: String,
            mode: String,
            code: String
    ) {
        launchCatchError(coroutineContext, {
            TkpdIdlingResource.increment()
            val params = otpValidateUseCase2FA.getParams(otpType = otpType, validateToken = validateToken, userIdEnc = userIdEnc, mode = mode, code = code)
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
            val params = otpValidateUseCase.getParams(code, otpType, msisdn, fpData, getSL, email, mode, signature, timeUnix, userId)
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

    public override fun onCleared() {
        _onClearedViewModel.value = Unit
        super.onCleared()
    }
}