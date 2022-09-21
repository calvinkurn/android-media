package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.GetUserProfileUpdateUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.GetUserProfileValidateUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileUpdateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileUpdateParam
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileValidateModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileValidateParam
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.Register
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterCheckUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetRegisterV2AndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class RedefineRegisterInputPhoneViewModel @Inject constructor(
    private val getRegisterCheckUseCase: GetRegisterCheckUseCase,
    private val getUserInfoAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    private val getRegisterV2AndSaveSessionUseCase: GetRegisterV2AndSaveSessionUseCase,
    private val getUserProfileUpdateUseCase: GetUserProfileUpdateUseCase,
    private val getUserProfileValidateUseCase: GetUserProfileValidateUseCase,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var phoneError = RESOURCE_NOT_CHANGED

    private val _formState = SingleLiveEvent<Int>()
    val formState: LiveData<Int> get() = _formState

    private val _isRegisteredPhone = SingleLiveEvent<RegistrationPhoneState>()
    val isRegisteredPhone: LiveData<RegistrationPhoneState> get() = _isRegisteredPhone

    private val _registerV2 = MutableLiveData<Result<Register>>()
    val registerV2: LiveData<Result<Register>> get() = _registerV2

    private val _getUserInfo = MutableLiveData<Result<ProfilePojo>>()
    val getUserInfo: LiveData<Result<ProfilePojo>> get() = _getUserInfo

    private val _submitRegisterLoading = SingleLiveEvent<Boolean>()
    val submitRegisterLoading: LiveData<Boolean> get() = _submitRegisterLoading

    private val _userPhoneUpdate = MutableLiveData<Result<UserProfileUpdateModel>>()
    val userPhoneUpdate: LiveData<Result<UserProfileUpdateModel>> get() = _userPhoneUpdate

    private val _userProfileValidate = MutableLiveData<Result<UserProfileValidateModel>>()
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
                NOTHING_RESOURCE
            }
        }

        _formState.value = phoneError
    }

    private fun isPhoneNumberValid(): Boolean {
        return phoneError == NOTHING_RESOURCE
    }

    fun submitForm(phone: String) {
        if (isPhoneNumberValid()) {
            registerCheck(phone)
        } else {
            validatePhone(phone)
        }
    }

    private fun registerCheck(phone: String) {
        _isRegisteredPhone.value = RegistrationPhoneState.Loading()
        launchCatchError(coroutineContext, {
            val response = getRegisterCheckUseCase(phone)

            _isRegisteredPhone.value = when {
                response.data.errors.isNotEmpty() -> {
                    RegistrationPhoneState.Ineligible(message = response.data.errors[0])
                }
                response.data.isExist -> {
                    RegistrationPhoneState.Registered(phoneNumber = phone)
                }
                else -> {
                    RegistrationPhoneState.Unregistered(phoneNumber = phone)
                }
            }

        }, {
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

    //token must get from OTP type 126
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
            userSession.clearToken()
            _registerV2.value = Fail(it)
        })
    }

    fun userProfileValidate(email: String, phone: String) {
        launchCatchError(coroutineContext, {
            val param = UserProfileValidateParam(
                email = email,
                phone = phone
            )
            val response = getUserProfileValidateUseCase(param)

            _userProfileValidate.value = Success(response)
        }, {
            _userProfileValidate.value = Fail(it)
        })
    }

    //token must get from OTP type 11
    fun userProfileUpdate(phone: String, token: String) {
        launchCatchError(coroutineContext, {

            val param = UserProfileUpdateParam(
                phone = phone,
                currentValidateToken = token
            )

            val response = getUserProfileUpdateUseCase(param)

            _userPhoneUpdate.value = Success(response)
        }, {
            _userPhoneUpdate.value = Fail(it)
        })
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
    }

}