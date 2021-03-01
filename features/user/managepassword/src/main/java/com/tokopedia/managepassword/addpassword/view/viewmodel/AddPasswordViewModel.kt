package com.tokopedia.managepassword.addpassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.managepassword.common.ManagePasswordConstant
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddPasswordViewModel @Inject constructor(
        private val usecase: AddPasswordUseCase,
        private val getProfileCompletionUseCase: GetProfileCompletionUseCase,
        private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<AddPasswordResponseModel>>()
    val response: LiveData<Result<AddPasswordResponseModel>>
        get() = _response

    private val _validatePassword = MutableLiveData<Result<String>>()
    val validatePassword: LiveData<Result<String>>
        get() = _validatePassword

    private val _validatePasswordConfirmation = MutableLiveData<Result<String>>()
    val validatePasswordConfirmation: LiveData<Result<String>>
        get() = _validatePasswordConfirmation

    private val _profileData = MutableLiveData<Result<ProfileDataModel>>()
    val profileDataModel: LiveData<Result<ProfileDataModel>>
        get() = _profileData

    fun checkPassword() {
        getProfileCompletionUseCase.getData(onSuccess = {
            _profileData.postValue(Success(it))
        }, onError = {
            _profileData.postValue(Fail(it))
        })
    }

    fun createPassword(password: String, confirmationPassword: String) {
        launchCatchError(coroutineContext, {
            val key = generatePublicKeyUseCase.executeOnBackground().keyData
            if(key.hash.isNotEmpty()) {
                usecase.params = createRequestParams(password, confirmationPassword, key.hash)
                usecase.submit(onSuccess = {
                    if (it.addPassword.isSuccess) {
                        _response.postValue(Success(it))
                    } else {
                        _response.postValue(Fail(Throwable(it.addPassword.errorMessage)))
                    }
                }, onError = {
                    _response.postValue(Fail(it))
                })
            } else {
                _response.postValue(Fail(Throwable("")))
            }
        }, {
            _response.postValue(Fail(it))
        })
    }

    fun validatePassword(password: CharSequence) {
        when {
            password.isEmpty() -> {
                _validatePassword.postValue(Fail(Throwable(ERROR_FIELD_REQUIRED)))
            }
            password.length < MIN_COUNT -> {
                _validatePassword.postValue(Fail(Throwable(ERROR_MIN_CHAR)))
            }
            password.length > MAX_COUNT -> {
                _validatePassword.postValue(Fail(Throwable(ERROR_MAX_CHAR)))
            }
            else -> {
                _validatePassword.postValue(Success(""))
            }
        }
    }

    fun validatePasswordConfirmation(password: CharSequence) {
        when {
            password.isEmpty() -> {
                _validatePasswordConfirmation.postValue(Fail(Throwable(ERROR_FIELD_REQUIRED)))
            }
            password.length < MIN_COUNT -> {
                _validatePasswordConfirmation.postValue(Fail(Throwable(ERROR_MIN_CHAR)))
            }
            password.length > MAX_COUNT -> {
                _validatePasswordConfirmation.postValue(Fail(Throwable(ERROR_MAX_CHAR)))
            }
            else -> {
                _validatePasswordConfirmation.postValue(Success(""))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        usecase.cancelJobs()
    }

    companion object {
        private const val MIN_COUNT = 8
        private const val MAX_COUNT = 32
        private const val ERROR_FIELD_REQUIRED = "Harus diisi"
        private const val ERROR_MIN_CHAR = "Minimum $MIN_COUNT karakter"
        private const val ERROR_MAX_CHAR = "Maksimum $MAX_COUNT karakter"

        fun createRequestParams(password: String, confirmationPassword: String, hash: String): Map<String, Any> {
            return mapOf(
                    ManagePasswordConstant.PARAM_PASSWORD to password,
                    ManagePasswordConstant.PARAM_PASSWORD_CONFIRMATION to confirmationPassword,
                    ManagePasswordConstant.PARAM_HASH to hash
            )
        }
    }
}