package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants.EMPTY_RESOURCE
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants.INITIAL_RESOURCE
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.local.RegisterPreferences
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.GetUserProfileUpdateUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.GetUserProfileValidateUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileUpdateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.param.UserProfileUpdateParam
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileValidateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.param.UserProfileValidateParam
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.Register
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterCheckUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterV2AndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class RedefineRegisterInputPhoneViewModel @Inject constructor(
    private val getRegisterCheckUseCase: GetRegisterCheckUseCase,
    private val getUserInfoAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    private val getRegisterV2AndSaveSessionUseCase: GetRegisterV2AndSaveSessionUseCase,
    private val getUserProfileUpdateUseCase: GetUserProfileUpdateUseCase,
    private val getUserProfileValidateUseCase: GetUserProfileValidateUseCase,
    private val registerPreferences: RegisterPreferences,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var phoneError = INITIAL_RESOURCE

    private val _formState = SingleLiveEvent<Int>()
    val formState: LiveData<Int> get() = _formState

    private val _submitPhoneLoading = MutableLiveData<Boolean>()
    val submitPhoneLoading: LiveData<Boolean> get() = _submitPhoneLoading

    private val _isRegisteredPhone = SingleLiveEvent<RegistrationPhoneState>()
    val isRegisteredPhone: LiveData<RegistrationPhoneState> get() = _isRegisteredPhone

    private val _registerV2 = SingleLiveEvent<Result<Register>>()
    val registerV2: LiveData<Result<Register>> get() = _registerV2

    private val _getUserInfo = SingleLiveEvent<Result<ProfilePojo>>()
    val getUserInfo: LiveData<Result<ProfilePojo>> get() = _getUserInfo

    private val _submitRegisterLoading = MutableLiveData<Boolean>()
    val submitRegisterLoading: LiveData<Boolean> get() = _submitRegisterLoading

    private val _userPhoneUpdate = SingleLiveEvent<Result<UserProfileUpdateModel>>()
    val userPhoneUpdate: LiveData<Result<UserProfileUpdateModel>> get() = _userPhoneUpdate

    private val _userProfileValidate = SingleLiveEvent<Result<UserProfileValidateModel>>()
    val userProfileValidate: LiveData<Result<UserProfileValidateModel>> get() = _userProfileValidate

    fun validatePhone(phone: String) {
        phoneError = when {
            phone.isEmpty() -> {
                R.string.register_email_message_must_be_filled
            }
            RegisterUtil.isPhoneTooShortLength(phone) -> {
                R.string.register_email_input_phone_min_length_error
            }
            RegisterUtil.isPhoneExceedMaximumLength(phone) -> {
                R.string.register_email_input_phone_max_length_error
            }
            else -> {
                EMPTY_RESOURCE
            }
        }

        _formState.value = phoneError
    }

    private fun isPhoneNumberValid(): Boolean {
        return phoneError == EMPTY_RESOURCE
    }

    fun submitForm(phone: String, email: String, isRequiredInputPhone: Boolean) {
        if (isPhoneNumberValid()) {
            if (isRequiredInputPhone) {
                registerCheck(phone)
            } else {
                userProfileValidate(
                    UserProfileValidateParam(
                        email = email,
                        phone = phone
                    )
                )
            }
        } else {
            validatePhone(phone)
        }
    }

    private fun registerCheck(phone: String) {
        _submitPhoneLoading.value = true
        launchCatchError(coroutineContext, {
            val response = getRegisterCheckUseCase(phone)

            _isRegisteredPhone.value = when {
                response.data.errors.isNotEmpty() -> {
                    RegistrationPhoneState.Ineligible(message = response.data.errors.first())
                }
                response.data.isExist -> {
                    RegistrationPhoneState.Registered(phoneNumber = phone)
                }
                else -> {
                    RegistrationPhoneState.Unregistered(phoneNumber = phone)
                }
            }
            _submitPhoneLoading.value = false
        }, {
            _submitPhoneLoading.value = false
            _isRegisteredPhone.value = RegistrationPhoneState.Failed(throwable = it)
        })
    }

    fun getUserInfo() {
        _submitRegisterLoading.value = true
        launchCatchError(coroutineContext, {
            _getUserInfo.value = getUserInfoAndSaveSessionUseCase(Unit)

            _submitRegisterLoading.value = false
        }, {
            _submitRegisterLoading.value = false
            _getUserInfo.value = Fail(it)
        })
    }

    // token must get from OTP type 126
    fun registerV2(
        registerV2Param: RegisterV2Param
    ) {
        _submitRegisterLoading.value = true
        launchCatchError(coroutineContext, {

            val result = getRegisterV2AndSaveSessionUseCase(registerV2Param)

            _registerV2.value = result
            _submitRegisterLoading.value = false
        }, {
            _submitRegisterLoading.value = false
            _registerV2.value = Fail(it)
        })
    }

    private fun userProfileValidate(userProfileValidateParam: UserProfileValidateParam) {
        _submitPhoneLoading.value = true
        launchCatchError(coroutineContext, {

            val response = getUserProfileValidateUseCase(userProfileValidateParam)

            _submitPhoneLoading.value = false
            _userProfileValidate.value = Success(response)
        }, {
            _submitPhoneLoading.value = false
            _userProfileValidate.value = Fail(it)
        })
    }

    // token must get from OTP type 11
    fun userProfileUpdate(userProfileUpdateParam: UserProfileUpdateParam) {
        launchCatchError(coroutineContext, {

            val response = getUserProfileUpdateUseCase(userProfileUpdateParam)

            _userPhoneUpdate.value = Success(response)
        }, {
            _userPhoneUpdate.value = Fail(it)
        })
    }

    fun saveFirstInstallTime() {
        launch {
            try {
                registerPreferences.saveFirstInstallTime()
            } catch (_: Exception) {}
        }
    }

}

sealed class RegistrationPhoneState(val message: String = "", val throwable: Throwable? = null) {
    class Unregistered(phoneNumber: String) : RegistrationPhoneState(message = phoneNumber)
    class Registered(phoneNumber: String) : RegistrationPhoneState(message = phoneNumber)
    class Ineligible(message: String) : RegistrationPhoneState(message = message)
    class Failed(throwable: Throwable) : RegistrationPhoneState(throwable = throwable)
}
