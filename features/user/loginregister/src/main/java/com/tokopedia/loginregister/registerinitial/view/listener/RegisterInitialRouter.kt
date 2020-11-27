package com.tokopedia.loginregister.registerinitial.view.listener

interface RegisterInitialRouter {
    fun goToRegisterEmailPage()
    fun goToRegisterGoogle()
    fun goToRegisterFacebook()
    fun goToLoginPage()
    fun goToLoginRegisteredPhoneNumber(phone: String)
    fun goToRegisterWithPhoneNumber(phone: String)
    fun goToOTPRegisterEmail(email: String)
    fun goToOTPActivateEmail(email: String)
    fun onSuccessRegister()
    fun goToRegisterEmailPageWithEmail(email: String, token: String, source: String)
}