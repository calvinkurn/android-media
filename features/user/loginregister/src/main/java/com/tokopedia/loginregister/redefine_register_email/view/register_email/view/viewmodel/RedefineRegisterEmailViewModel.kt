package com.tokopedia.loginregister.redefine_register_email.view.register_email.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefine_register_email.view.register_email.domain.GenerateKeyUseCase
import com.tokopedia.loginregister.redefine_register_email.view.register_email.domain.ValidateUserDataUseCase
import com.tokopedia.loginregister.redefine_register_email.view.register_email.domain.data.ValidateUserData
import com.tokopedia.loginregister.redefine_register_email.view.register_email.domain.data.ValidateUserDataParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent

class RedefineRegisterEmailViewModel @Inject constructor(
    private val generateKeyUseCase: GenerateKeyUseCase,
    private val validateUserDataUseCase: ValidateUserDataUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val state = RedefineEmailFormState()
    private val _formState = SingleLiveEvent<RedefineEmailFormState>()
    val formState: LiveData<RedefineEmailFormState> get() = _formState
    private var _isLoading = SingleLiveEvent<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading
    private val _validateUserData = SingleLiveEvent<Result<ValidateUserData>>()
    val validateUserData: LiveData<Result<ValidateUserData>> get() = _validateUserData
    private var _currentEmail = ""
    val currentEmail get() = _currentEmail
    private var _currentPassword = ""
    val currentPassword get() = _currentPassword
    private var _currentName = ""
    val currentName get() = _currentName
    private var _currentHash = ""
    val currentHash get() = _currentHash
    private var _encryptedPassword = ""
    val encryptedPassword get() = _encryptedPassword

    fun validateEmail(email: String, setValue: Boolean = true) {
        state.emailError = when {
            email.isEmpty() -> {
                R.string.error_field_required
            }
            !RegisterUtil.isValidEmail(email) -> {
                R.string.register_email_message_email_not_valid
            }
            else -> {
                NOTHING_RESOURCE
            }
        }
        if (setValue) _formState.value = state
    }

    fun validatePassword(password: String, setValue: Boolean = true) {
        state.passwordError = when {
            password.isEmpty() -> {
                R.string.error_field_required
            }
            RegisterUtil.isPasswordTooShortLength(password) -> {
                R.string.error_minimal_password
            }
            RegisterUtil.isPasswordExceedMaximumLength(password) -> {
                R.string.error_maximal_password
            }
            else -> {
                NOTHING_RESOURCE
            }
        }
        if (setValue) _formState.value = state
    }

    fun validateName(name: String, setValue: Boolean = true) {
        state.nameError = when {
            name.isEmpty() -> {
                R.string.error_field_required
            }
            RegisterUtil.isBelowMinChar(name) -> {
                R.string.register_email_message_name_min_length_error
            }
            RegisterUtil.checkRegexNameLocal(name) -> {
                R.string.register_email_message_name_character_error
            }
            RegisterUtil.isExceedMaxCharacter(name) -> {
                R.string.register_email_message_name_max_length_error
            }
            else -> NOTHING_RESOURCE
        }
        if (setValue) _formState.value = state
    }

    private fun isAllDataValid(): Boolean {
        return isDataValid(state.emailError) && isDataValid(state.passwordError) && isDataValid(state.nameError)
    }

    private fun isDataValid(stringResource: Int): Boolean {
        return stringResource == NOTHING_RESOURCE
    }

    fun submitForm(email: String, password: String, name: String) {
        if (isAllDataValid()) {
            if (_isLoading.value == false || _isLoading.value == null) {

                //save value
                _currentEmail = email
                _currentPassword = password
                _currentName = name

                validateUserData()
            }
        } else {
            validateEmail(email, false)
            validatePassword(password, false)
            validateName(name, false)
            _formState.value = state
        }
    }

    private fun validateUserData() {
        _isLoading.value = true
        launchCatchError(coroutineContext, {
            val keyData = generateKeyUseCase(Unit).generateKey
            _encryptedPassword = RsaUtils.encrypt(_currentPassword, keyData.key.decodeBase64(), true)
            _currentHash = keyData.h

            val param = ValidateUserDataParam(
                email = currentEmail,
                fullName = currentName,
                password = encryptedPassword,
                hash = currentHash
            )
            val response = validateUserDataUseCase(param)
            _validateUserData.value = Success(response.validateUserData)

            _isLoading.value = false
        }, {
            _validateUserData.value = Fail(it)
            _isLoading.value = false
        })
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
    }

}