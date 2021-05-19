package com.tokopedia.loginregister.login.behaviour.case

import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import org.junit.Before
import org.junit.Test

class LoginNegativeCase: LoginBase() {

    @Before
    fun defaultResponse() {
        setDefaultDiscover()
        setRegisterCheckDefaultResponse()
        launchDefaultFragment()
    }

//    @Test
//    /* Show error text when input text is empty */
//    fun showErrorText_ifEmpty() {
//        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
//        deleteEmailOrPhoneInput()
//        clickSubmit()
//
//        shouldBeDisplayed(R.id.tv_error)
//    }

    @Test
    /* Disable button "Selanjutnya" when input text is empty */
    fun disableNextButton_ifEmpty() {
        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        deleteEmailOrPhoneInput()

        shouldBeDisabled(R.id.register_btn)
    }

    @Test
    /* Disable button "Selanjutnya" when input text is not valid email */
    fun disableNextButton_ifNotValidEmail() {

        inputEmailOrPhone("yoris.prayogo")

        shouldBeDisabled(R.id.register_btn)
    }

    @Test
    /* Disable button "Selanjutnya" when input text length is not valid for phone number */
    fun disableNextButton_ifPhoneNumberInvalid() {

        inputEmailOrPhone("08224")
        deleteEmailOrPhoneInput()

        shouldBeDisabled(R.id.register_btn)
    }

    @Test
    /* Display error text when user click on Masuk button while password is empty */
    fun displayError_IfPasswordEmpty() {
        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()
        clickSubmit()

        isDisplayingGivenText(R.id.textinput_helper_text, "Kata sandi harus diisi")
    }

}