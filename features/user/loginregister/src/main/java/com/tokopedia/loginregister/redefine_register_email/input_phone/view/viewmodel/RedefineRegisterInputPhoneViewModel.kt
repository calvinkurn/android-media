package com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.RegisterUtil
import com.tokopedia.loginregister.redefine_register_email.common.GetProfileInfoAbstraction
import com.tokopedia.loginregister.redefine_register_email.common.RegisterV2Abstraction
import com.tokopedia.loginregister.redefine_register_email.domain.GenerateKeyUseCase
import com.tokopedia.loginregister.redefine_register_email.domain.GetUserInfoUseCase
import com.tokopedia.loginregister.redefine_register_email.domain.RegisterV2UseCase
import com.tokopedia.loginregister.redefine_register_email.domain.data.GetUserInfoModel
import com.tokopedia.loginregister.redefine_register_email.domain.data.Register
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.RegisterCheckUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class RedefineRegisterInputPhoneViewModel @Inject constructor(
    private val registerCheckUseCase: RegisterCheckUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val registerV2UseCase: RegisterV2UseCase,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

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
                    RegisteredPhoneState.Unregistered(phoneNumber = response.data.view)
                }
            }

        }, {
            _isRegisteredPhone.value = RegisteredPhoneState.Failed(throwable = it)
        })
    }

    fun getUserInfo() {
        _submitRegisterLoading.value = true
        launchCatchError(coroutineContext, {
            _getUserInfo.value = object : GetProfileInfoAbstraction(getUserInfoUseCase, userSession) {}.data()

            _submitRegisterLoading.value = false
        }, {
            _submitRegisterLoading.value = false
            _getUserInfo.value = Fail(it)
        })
    }

    fun registerV2(email: String = "", phone: String = "", fullName: String, password: String, validateToken: String, hash: String) {
        _submitRegisterLoading.value = true
        launchCatchError(coroutineContext, {
            _registerV2.value = object : RegisterV2Abstraction(email, phone, fullName, password, validateToken, hash, registerV2UseCase, userSession) {
                override suspend fun loadUserInfo() {
                    getUserInfo()
                }
            }.data()

            _submitRegisterLoading.value = false
        },  {
            _submitRegisterLoading.value = false
            onFailedRegisterV2Request(it)
        })
    }

    private fun onFailedRegisterV2Request(throwable: Throwable) {
        userSession.clearToken()
        _registerV2.value = Fail(throwable)
    }

    companion object {
        const val NOTHING_RESOURCE = 0
        const val RESOURCE_NOT_CHANGED = -1
    }

}