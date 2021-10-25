package com.tokopedia.otp.silentverification.view.viewmodel

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResult
import com.tokopedia.otp.silentverification.domain.usecase.RequestSilentVerificationOtpUseCase
import com.tokopedia.otp.silentverification.domain.usecase.ValidateSilentVerificationUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by Yoris on 18/10/21.
 */

class SilentVerificationViewModel @Inject constructor(
    val requestSilentVerificationOtpUseCase: RequestSilentVerificationOtpUseCase,
    val validateSilentVerificationUseCase: ValidateSilentVerificationUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _validationResponse = MutableLiveData<Result<OtpValidateData>>()
    val validationResponse: LiveData<Result<OtpValidateData>>
        get() = _validationResponse

    private val _requestSilentVerificationResponse = MutableLiveData<Result<RequestSilentVerificationResult>>()
    val requestSilentVerificationResponse: LiveData<Result<RequestSilentVerificationResult>>
        get() = _requestSilentVerificationResponse

    private val _bokuVerificationResponse = MutableLiveData<Result<String>>()
    val bokuVerificationResponse: LiveData<Result<String>>
        get() = _bokuVerificationResponse

    fun requestSilentVerification(
        otpType: String,
        mode: String,
        msisdn: String,
        otpDigit: Int
    ) {
        launchCatchError(block = {
            val params = mapOf(
                RequestSilentVerificationOtpUseCase.PARAM_OTP_TYPE to otpType,
                RequestSilentVerificationOtpUseCase.PARAM_MODE to mode,
                RequestSilentVerificationOtpUseCase.PARAM_MSISDN to msisdn
//                RequestSilentVerificationOtpUseCase.PARAM_OTP_DIGIT to otpDigit
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
        correlationId: String) {
        launchCatchError(block = {
            val params = mapOf(
                ValidateSilentVerificationUseCase.PARAM_OTP_TYPE to otpType,
                ValidateSilentVerificationUseCase.PARAM_MODE to mode,
                ValidateSilentVerificationUseCase.PARAM_USERID to userId,
                ValidateSilentVerificationUseCase.PARAM_MSISDN to msisdn,
                ValidateSilentVerificationUseCase.PARAM_ASSOCIATION_ID to correlationId
            )
            val result = validateSilentVerificationUseCase(params)
            _validationResponse.value = Success(result.data)
        }, onError = {
            _validationResponse.value = Fail(it)
        })
    }

//    fun getUserInfo() {
//        launchCatchError(block = {
//            val result = getUserInfoUseCase(Unit)
//            _validationResponse.value = Success(result.data)
//        }, onError = {
//            _validationResponse.value = Fail(it)
//        })
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun verify(context: Context, url: String) {
        val connectivityManager =
            context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
        connectivityManager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val okHttpClient: OkHttpClient =
                    OkHttpClient.Builder().socketFactory(network.socketFactory).build()
                val req: Request = Request.Builder()
                    .url(url)
                    .build()
                try {
                    val response: Response = okHttpClient.newCall(req).execute()
                    val result = response.body()?.string() ?: ""
                    _bokuVerificationResponse.value = Success(result)
//                    Log.i(
//                        "bokunetworkresponse", """
//                         doAPIonCellularNetwork RESULT:
//                         ${response.body?.string()}
//                         """.trimIndent()
//                    )
                } catch (ex: Exception) {
                    println("bokunetworkresponse $ex")
                    ex.printStackTrace()
                }
            }
        })
    }
}