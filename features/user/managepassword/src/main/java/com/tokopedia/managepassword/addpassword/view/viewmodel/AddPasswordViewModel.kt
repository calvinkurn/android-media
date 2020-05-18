package com.tokopedia.managepassword.addpassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordDataModel
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddPasswordViewModel @Inject constructor(
        private val usecase: AddPasswordUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<AddPasswordDataModel>>()
    val response: LiveData<Result<AddPasswordDataModel>>
        get() = _response

    private val _validate = MutableLiveData<Result<String>>()
    val validate: LiveData<Result<String>>
        get() = _validate

    fun createPassword(password: String) {
        launchCatchError(coroutineContext, {
            usecase.params = generateParam(password, userSession)
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

    fun validatePassword(password: CharSequence) {
        when {
            password.isEmpty() -> {
                _validate.postValue(Fail(Throwable(ERROR_FIELD_REQUIRED)))
            }
            password.length < MIN_COUNT -> {
                _validate.postValue(Fail(Throwable(ERROR_MIN_CHAR)))
            }
            password.length > MAX_COUNT -> {
                _validate.postValue(Fail(Throwable(ERROR_MAX_CHAR)))
            }
            else -> {
                _validate.postValue(Success(""))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        usecase.cancelJobs()
    }

    companion object {
        private const val PARAM_OS_TYPE: String = "os_type"
        private const val OS_TYPE_ANDROID: String = "1"

        private const val MIN_COUNT = 6
        private const val MAX_COUNT = 32
        private const val ERROR_FIELD_REQUIRED = "Harus diisi"
        private const val ERROR_MIN_CHAR = "Minimum 6 karakter"
        private const val ERROR_MAX_CHAR = "Maksimum 32 karakter"


        private fun generateParam(email: String, userSession: UserSessionInterface): RequestParams {
            val param = RequestParams.create()
            param.putAllString(AuthHelper.generateParamsNetwork(userSession.userId, userSession.deviceId, TKPDMapParam()))
            param.putString(PARAM_EMAIL, email)
            param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            return param
        }
    }
}