package com.tokopedia.loginregister.redefine_register_email.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefine_register_email.domain.GenerateKeyUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent

class RedefineRegisterEmailViewModel @Inject constructor(
    private val generateKeyUseCase: GenerateKeyUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val state = FormState()
    private val _formState = SingleLiveEvent<FormState>()
    val formState: LiveData<FormState> get() = _formState
    private val _isRegisteredEmail = SingleLiveEvent<Boolean>()
    val isRegisteredEmail: LiveData<Boolean> get() = _isRegisteredEmail

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
            _isRegisteredEmail.value = true
            generateKey()
        } else {
            validateEmail(email, false)
            validatePassword(password, false)
            validateName(name, false)
            _formState.value = state
        }
    }

    private fun generateKey() {
        launchCatchError(coroutineContext, {
            val response = generateKeyUseCase(Unit)
        }, {

        })
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
    }

}