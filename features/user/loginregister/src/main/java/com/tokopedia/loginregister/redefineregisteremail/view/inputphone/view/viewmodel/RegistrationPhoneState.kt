package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.viewmodel

sealed class RegistrationPhoneState(val message: String = "", val throwable: Throwable? = null) {
    class Unregistered(phoneNumber: String) : RegistrationPhoneState(message = phoneNumber)
    class Registered(phoneNumber: String) : RegistrationPhoneState(message = phoneNumber)
    class Loading : RegistrationPhoneState()
    class Ineligible(message: String) : RegistrationPhoneState(message = message)
    class Failed(throwable: Throwable) : RegistrationPhoneState(throwable = throwable)
}