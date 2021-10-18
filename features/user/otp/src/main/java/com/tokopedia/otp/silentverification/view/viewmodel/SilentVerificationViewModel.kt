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
import com.tokopedia.otp.silentverification.domain.model.ValidateSilentVerificationResult
import com.tokopedia.otp.silentverification.domain.usecase.RequestSilentVerificationUseCase
import com.tokopedia.otp.silentverification.domain.usecase.ValidateSilentVerificationUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoUseCase
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
    val requestSilentVerificationUseCase: RequestSilentVerificationUseCase,
    val validateSilentVerificationUseCase: ValidateSilentVerificationUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _validationResponse = MutableLiveData<Result<ValidateSilentVerificationResult>>()
    val validationResponse: LiveData<Result<ValidateSilentVerificationResult>>
        get() = _validationResponse

    private val _requestSilentVerificationResponse = MutableLiveData<Result<RequestSilentVerificationResult>>()
    val requestSilentVerificationResponse: LiveData<Result<RequestSilentVerificationResult>>
        get() = _requestSilentVerificationResponse

    private val _bokuVerificationResponse = MutableLiveData<Result<String>>()
    val bokuVerificationResponse: LiveData<Result<String>>
        get() = _bokuVerificationResponse

    fun requestSilentVerification(phoneNo: String) {
        launchCatchError(block = {
            val result = requestSilentVerificationUseCase(phoneNo)
            _requestSilentVerificationResponse.value = Success(result.data)
        }, onError = {
            _requestSilentVerificationResponse.value = Fail(it)
        })
    }

    fun validate(phoneNo: String, correlationId: String) {
        launchCatchError(block = {
            val params = mapOf(
                ValidateSilentVerificationUseCase.PARAM_MSISDN to phoneNo,
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