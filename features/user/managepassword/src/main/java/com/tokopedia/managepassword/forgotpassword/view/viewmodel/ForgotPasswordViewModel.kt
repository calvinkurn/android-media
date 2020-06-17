package com.tokopedia.managepassword.forgotpassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.managepassword.common.ManagePasswordConstant
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordRequestModel
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordResponseModel
import com.tokopedia.managepassword.forgotpassword.domain.usecase.ForgotPasswordUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
        private val usecase: ForgotPasswordUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<ForgotPasswordResponseModel>>()
    val response: LiveData<Result<ForgotPasswordResponseModel>>
        get() = _response

    fun resetPassword(email: String) {
        launchCatchError(coroutineContext, {
            usecase.params = generateParam(email)
            usecase.sendRequest(onSuccess = {
                if (it.resetPassword.isSuccess) {
                    _response.postValue(Success(it))
                } else {
                    _response.postValue(Fail(Throwable(it.resetPassword.message)))
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
        private fun generateParam(email: String): Map<String, Any> {
            return mapOf(ManagePasswordConstant.PARAM_INPUT to ForgotPasswordRequestModel(email))
        }
    }
}