package com.tokopedia.managepassword.addpassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordData
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordParam
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordV2Params
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordV2UseCase
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddPasswordViewModel @Inject constructor(
    private val addPasswordUseCase: AddPasswordUseCase,
    private val addPasswordV2UseCase: AddPasswordV2UseCase,
    private val getProfileCompletionUseCase: GetProfileCompletionUseCase,
    private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _response = MutableLiveData<Result<AddPasswordData>>()
    val response: LiveData<Result<AddPasswordData>>
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
        launchCatchError(coroutineContext, {
            val response = getProfileCompletionUseCase(Unit)
            _profileData.value = Success(response)
        }, {
            _profileData.value = Fail(it)
        })
    }

    fun createPassword(password: String, confirmationPassword: String) {
        launchCatchError(coroutineContext, {
            val response = addPasswordUseCase(
                AddPasswordParam(
                    password = password,
                    passwordConfirmation = confirmationPassword
                )
            )

            if (response.addPassword.isSuccess) {
                _response.value = Success(response.addPassword)
            } else {
                _response.value = Fail(Throwable(response.addPassword.errorMessage))
            }
        }, {
            _response.value = Fail(it)
        })
    }

    fun createPasswordV2(password: String, confirmationPassword: String) {
        launchCatchError(coroutineContext, {
            val key = generatePublicKeyUseCase().keyData
            if (key.hash.isNotEmpty()) {
                val decodedKey = key.key.decodeBase64()
                val encryptedPassword = RsaUtils.encrypt(password, decodedKey, true)
                val encryptedConfirmPassword = RsaUtils.encrypt(confirmationPassword, decodedKey, true)

                val response = addPasswordV2UseCase(
                    AddPasswordV2Params(
                        password = encryptedPassword,
                        passwordConfirmation = encryptedConfirmPassword,
                        hash = key.hash
                    )
                )

                if (response.addPassword.isSuccess) {
                    _response.value = Success(response.addPassword)
                } else {
                    _response.value = Fail(Throwable(response.addPassword.errorMessage))
                }
            } else {
                _response.value = Fail(Throwable(""))
            }
        }, {
            _response.value = Fail(it)
        })
    }

    fun validatePassword(password: CharSequence) {
        when {
            password.isEmpty() -> {
                _validatePassword.value = Fail(Throwable(ERROR_FIELD_REQUIRED))
            }
            password.length < MIN_COUNT -> {
                _validatePassword.value = Fail(Throwable(ERROR_MIN_CHAR))
            }
            password.length > MAX_COUNT -> {
                _validatePassword.value = Fail(Throwable(ERROR_MAX_CHAR))
            }
            else -> {
                _validatePassword.value = Success("")
            }
        }
    }

    fun validatePasswordConfirmation(password: CharSequence) {
        when {
            password.isEmpty() -> {
                _validatePasswordConfirmation.value = Fail(Throwable(ERROR_FIELD_REQUIRED))
            }
            password.length < MIN_COUNT -> {
                _validatePasswordConfirmation.value = Fail(Throwable(ERROR_MIN_CHAR))
            }
            password.length > MAX_COUNT -> {
                _validatePasswordConfirmation.value = Fail(Throwable(ERROR_MAX_CHAR))
            }
            else -> {
                _validatePasswordConfirmation.value = Success("")
            }
        }
    }

    companion object {
        private const val MIN_COUNT = 8
        private const val MAX_COUNT = 32
        private const val ERROR_FIELD_REQUIRED = "Harus diisi"
        private const val ERROR_MIN_CHAR = "Minimum $MIN_COUNT karakter"
        private const val ERROR_MAX_CHAR = "Maksimum $MAX_COUNT karakter"
    }
}
