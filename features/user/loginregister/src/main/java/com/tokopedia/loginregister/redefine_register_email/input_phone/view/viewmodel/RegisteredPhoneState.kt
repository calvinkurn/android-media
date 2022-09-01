package com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel

sealed class RegisteredPhoneState(val message: String = "", val throwable: Throwable? = null) {
    class Unregistered(phoneNumber: String) : RegisteredPhoneState(message = phoneNumber)
    class Registered(phoneNumber: String) : RegisteredPhoneState(message = phoneNumber)
    class Loading : RegisteredPhoneState()
    class Ineligible(message: String) : RegisteredPhoneState(message = message)
    class Failed(throwable: Throwable) : RegisteredPhoneState(throwable = throwable)
}