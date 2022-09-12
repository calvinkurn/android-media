package com.tokopedia.loginregister.redefine_register_email.view.input_phone.view.viewmodel

sealed class RegistrationPhoneState(val message: String = "", val throwable: Throwable? = null) {
    class Unregistered(phoneNumber: String) : RegistrationPhoneState(message = phoneNumber)
    class Registration(phoneNumber: String) : RegistrationPhoneState(message = phoneNumber)
    class Loading : RegistrationPhoneState()
    class Ineligible(message: String) : RegistrationPhoneState(message = message)
    class Failed(throwable: Throwable) : RegistrationPhoneState(throwable = throwable)
}