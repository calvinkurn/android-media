package com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel

sealed class RegisteredPhoneState(val message: String = "", val throwable: Throwable? = null) {
    class Unregistered(message: String) : RegisteredPhoneState(message = message)
    class Registered : RegisteredPhoneState()
    class Loading: RegisteredPhoneState()
    class Failed(message: String) : RegisteredPhoneState(message = message)
    class Error(throwable: Throwable) : RegisteredPhoneState(throwable = throwable)
}