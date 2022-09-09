package com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefine_register_email.input_phone.abstraction.GetProfileInfoAbstraction
import com.tokopedia.loginregister.redefine_register_email.input_phone.abstraction.RegisterV2Abstraction
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.GetUserInfoUseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.RegisterV2UseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.GetUserInfoModel
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.Register
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.UserProfileUpdateUseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.UserProfileValidateUseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.UserProfileUpdateParam
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.UserProfileValidateParam
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class RedefineRegisterInputPhoneViewModel @Inject constructor(
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val registerV2UseCase: RegisterV2UseCase,
    private val userProfileUpdateUseCase: UserProfileUpdateUseCase,
    private val userProfileValidateUseCase: UserProfileValidateUseCase,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var phoneError = RESOURCE_NOT_CHANGED
    private val _formState = SingleLiveEvent<Int>()
    val formState: LiveData<Int> get() = _formState
    private val _isRegisteredPhone = SingleLiveEvent<RegisteredPhoneState>()
    val isRegisteredPhone: LiveData<RegisteredPhoneState> get() = _isRegisteredPhone
    private val _registerV2 = MutableLiveData<Result<Register>>()
    val registerV2: LiveData<Result<Register>> get() = _registerV2
    private val _getUserInfo = MutableLiveData<Result<GetUserInfoModel>>()
    val getUserInfo: LiveData<Result<GetUserInfoModel>> get() = _getUserInfo
    private val _submitRegisterLoading = SingleLiveEvent<Boolean>()
    val submitRegisterLoading: LiveData<Boolean> get() = _submitRegisterLoading
    private val _userPhoneUpdate = MutableLiveData<Result<Unit>>()
    val userPhoneUpdate: LiveData<Result<Unit>> get() = _userPhoneUpdate
    private val _userProfileValidate = MutableLiveData<Result<String>>()
    val userProfileValidate: LiveData<Result<String>> get() = _userProfileValidate

    fun validatePhone(phone: String) {
        phoneError = when {
            phone.isEmpty() -> {
                R.string.error_field_required
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
        _isRegisteredPhone.value = RegisteredPhoneState.Loading()
        launchCatchError(coroutineContext, {
            val response = registerCheckUseCase(phone)

            _isRegisteredPhone.value = when {
                response.data.errors.isNotEmpty() -> {
                    RegisteredPhoneState.Ineligible(message = response.data.errors[0])
                }
                response.data.isExist -> {
                    RegisteredPhoneState.Registered(phoneNumber = phone)
                }
                else -> {
                    RegisteredPhoneState.Unregistered(phoneNumber = phone)
                }
            }

        }, {
            _isRegisteredPhone.value = RegisteredPhoneState.Failed(throwable = it)
        })
    }

    fun getUserInfo() {
        _submitRegisterLoading.value = true
        launchCatchError(coroutineContext, {
            _getUserInfo.value =
                object : GetProfileInfoAbstraction(getUserInfoUseCase, userSession) {}.data()

            _submitRegisterLoading.value = false
        }, {
            _submitRegisterLoading.value = false
            _getUserInfo.value = Fail(it)
        })
    }

    //token must get from OTP type 126
    fun registerV2(
        email: String = "",
        phone: String = "",
        fullName: String,
        password: String,
        validateToken: String,
        hash: String
    ) {
        _submitRegisterLoading.value = true
        launchCatchError(coroutineContext, {
            _registerV2.value = object : RegisterV2Abstraction(
                email,
                phone,
                fullName,
                password,
                validateToken,
                hash,
                registerV2UseCase,
                userSession
            ) {
                override suspend fun loadUserInfo() {
                    getUserInfo()
                }
            }.data()

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

            val response = userProfileValidateUseCase(param)

            if (response.data.isValid) {
                _userProfileValidate.value = Success(phone)
            } else {
                val message = response.data.message
                _userProfileValidate.value = Fail(MessageErrorException(message))
            }

        }, {
            _userPhoneUpdate.value = Fail(it)
        })
    }

    //token must get from OTP type 11
    fun userProfileUpdate(phone: String, token: String) {
        launchCatchError(coroutineContext, {

            val param = UserProfileUpdateParam(
                phone = phone,
                currValidateToken = token
            )

            val response = userProfileUpdateUseCase(param)

            if (response.data.errors.isEmpty()) {
                _userPhoneUpdate.value = Success(Unit)
            } else {
                val messageError = response.data.errors.first()
                _userPhoneUpdate.value = Fail(MessageErrorException(messageError))
            }

        }, {
            _userPhoneUpdate.value = Fail(it)
        })
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
    }

}