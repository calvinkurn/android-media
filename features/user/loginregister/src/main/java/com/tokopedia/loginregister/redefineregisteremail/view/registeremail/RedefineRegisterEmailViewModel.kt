package com.tokopedia.loginregister.redefineregisteremail.view.registeremail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.encryption.security.RsaUtils
import com.tokopedia.encryption.security.decodeBase64
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants.EMPTY_RESOURCE
import com.tokopedia.sessioncommon.domain.usecase.GenerateKeyUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.ValidateUserDataUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserData
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserDataParam
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

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val _validateUserData = SingleLiveEvent<Result<ValidateUserData>>()
    val validateUserData: LiveData<Result<ValidateUserData>> get() = _validateUserData

    var currentEmail = ""
        private set

    var currentPassword = ""
        private set

    var currentName = ""
        private set

    var currentHash = ""
        private set

    var encryptedPassword = ""
        private set

    fun validateEmail(email: String, setValue: Boolean = true) {
        currentEmail = email
        state.emailError = when {
            email.isEmpty() -> {
                R.string.register_email_message_must_be_filled
            }
            !RegisterUtil.isValidEmail(email) -> {
                R.string.register_email_message_email_not_valid
            }
            else -> {
                EMPTY_RESOURCE
            }
        }
        if (setValue) _formState.value = state
    }

    fun validatePassword(password: String, setValue: Boolean = true) {
        currentPassword = password
        state.passwordError = when {
            password.isEmpty() -> {
                R.string.register_email_message_must_be_filled
            }
            RegisterUtil.isPasswordTooShortLength(password) -> {
                R.string.error_minimal_password
            }
            RegisterUtil.isPasswordExceedMaximumLength(password) -> {
                R.string.error_maximal_password
            }
            else -> {
                EMPTY_RESOURCE
            }
        }
        if (setValue) _formState.value = state
    }

    fun validateName(name: String, setValue: Boolean = true) {
        currentName = name
        state.nameError = when {
            name.isEmpty() -> {
                R.string.register_email_message_must_be_filled
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
            else -> EMPTY_RESOURCE
        }
        if (setValue) _formState.value = state
    }

    private fun isAllDataValid(): Boolean {
        return isDataValid(state.emailError) && isDataValid(state.passwordError) && isDataValid(state.nameError)
    }

    private fun isDataValid(stringResource: Int): Boolean {
        return stringResource == EMPTY_RESOURCE
    }

    fun submitForm(email: String, password: String, name: String) {
        if (isAllDataValid()) {
            validateUserData()
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
            val keyData = generateKeyUseCase(Unit).keyData
            encryptedPassword = RsaUtils.encrypt(currentPassword, keyData.key.decodeBase64(), true)
            currentHash = keyData.hash

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

}

data class RedefineEmailFormState (
    var emailError: Int = RedefineRegisterEmailConstants.INITIAL_RESOURCE,
    var passwordError: Int = RedefineRegisterEmailConstants.INITIAL_RESOURCE,
    var nameError: Int = RedefineRegisterEmailConstants.INITIAL_RESOURCE,
    var isAllDataValid: Boolean = false
)
