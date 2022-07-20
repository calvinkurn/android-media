package com.tokopedia.otp.silentverification.view.viewmodel

import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResult
import com.tokopedia.otp.silentverification.domain.usecase.GetEvUrlUseCase
import com.tokopedia.otp.silentverification.domain.usecase.RequestSilentVerificationOtpUseCase
import com.tokopedia.otp.silentverification.domain.usecase.ValidateSilentVerificationUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Yoris on 18/10/21.
 */

class SilentVerificationViewModel @Inject constructor(
    val requestSilentVerificationOtpUseCase: RequestSilentVerificationOtpUseCase,
    val validateSilentVerificationUseCase: ValidateSilentVerificationUseCase,
    val getEvUrlUsecase: GetEvUrlUseCase,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _validationResponse = MutableLiveData<Result<OtpValidateData>>()
    val validationResponse: LiveData<Result<OtpValidateData>>
        get() = _validationResponse

    private val _requestSilentVerificationResponse =
        MutableLiveData<Result<RequestSilentVerificationResult>>()
    val requestSilentVerificationResponse: LiveData<Result<RequestSilentVerificationResult>>
        get() = _requestSilentVerificationResponse

    private val _bokuVerificationResponse = MutableLiveData<Result<String>>()
    val bokuVerificationResponse: LiveData<Result<String>>
        get() = _bokuVerificationResponse

    fun requestSilentVerification(
        otpType: String,
        mode: String,
        msisdn: String,
        signature: String,
        timeUnix: String
    ) {
        launchCatchError(block = {
            val params = mapOf(
                RequestSilentVerificationOtpUseCase.PARAM_OTP_TYPE to otpType,
                RequestSilentVerificationOtpUseCase.PARAM_MODE to mode,
                RequestSilentVerificationOtpUseCase.PARAM_MSISDN to msisdn,
                RequestSilentVerificationOtpUseCase.PARAM_AUTHENTICITY_SIGNATURE to signature,
                RequestSilentVerificationOtpUseCase.PARAM_TIME_UNIX to timeUnix
            )
            val result = requestSilentVerificationOtpUseCase(params)
            _requestSilentVerificationResponse.value = Success(result.data)
        }, onError = {
            _requestSilentVerificationResponse.value = Fail(it)
        })
    }

    fun validate(
        otpType: String,
        msisdn: String,
        mode: String,
        userId: Int,
        tokenId: String,
        timeUnix: String,
        signature: String
    ) {
        launchCatchError(block = {
            val params = mapOf(
                ValidateSilentVerificationUseCase.PARAM_OTP_TYPE to otpType,
                ValidateSilentVerificationUseCase.PARAM_MODE to mode,
                ValidateSilentVerificationUseCase.PARAM_USERID to userId,
                ValidateSilentVerificationUseCase.PARAM_MSISDN to msisdn,
                ValidateSilentVerificationUseCase.PARAM_TOKEN_ID to tokenId,
                PARAM_TIME_UNIX_VALIDATE to timeUnix,
                RequestSilentVerificationOtpUseCase.PARAM_AUTHENTICITY_SIGNATURE to signature
            )
            val result = validateSilentVerificationUseCase(params)
            _validationResponse.value = Success(result.data)
        }, onError = {
            _validationResponse.value = Fail(it)
        })
    }

    fun verifyBoku(network: Network, url: String) {
        launch {
            try {
                getEvUrlUsecase.apply {
                    setNetworkSocketFactory(network)
                    setUrl(url)
                }
                val result = getEvUrlUsecase(Unit)
                _bokuVerificationResponse.value = Success(result)
            } catch (e: Throwable) {
                e.printStackTrace()
                _bokuVerificationResponse.value = Fail(e)
            }
        }
    }

    companion object {
        const val PARAM_TIME_UNIX_VALIDATE = "time_unix"
    }
}