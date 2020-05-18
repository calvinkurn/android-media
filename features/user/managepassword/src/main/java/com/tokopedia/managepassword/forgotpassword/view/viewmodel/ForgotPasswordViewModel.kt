package com.tokopedia.managepassword.forgotpassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordDataModel
import com.tokopedia.managepassword.forgotpassword.domain.usecase.ForgotPasswordUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
        private val usecase: ForgotPasswordUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<ForgotPasswordDataModel>>()
    val response: LiveData<Result<ForgotPasswordDataModel>>
        get() = _response

    fun resetPassword(email: String) {
        launchCatchError(coroutineContext, {
            usecase.params = generateParam(email, userSession)
            usecase.execute(onSuccess = {
                if (it.data?.is_success == 1) {
                    _response.postValue(Success(it))
                } else {
                    _response.postValue(Fail(Throwable(it.message_error?.get(0))))
                }
            }, onError = {
                _response.postValue(Fail(it))
            })
        }, {
            _response.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        super.onCleared()
        usecase.cancelJobs()
    }

    companion object {
        private const val PARAM_OS_TYPE: String = "os_type"
        private const val OS_TYPE_ANDROID: String = "1"

        private fun generateParam(email: String, userSession: UserSessionInterface): RequestParams {
            val param = RequestParams.create()
            param.putAllString(AuthHelper.generateParamsNetwork(userSession.userId, userSession.deviceId, TKPDMapParam()))
            param.putString(PARAM_EMAIL, email)
            param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            return param
        }
    }
}