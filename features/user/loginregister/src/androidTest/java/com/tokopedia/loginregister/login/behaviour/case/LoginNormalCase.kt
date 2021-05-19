package com.tokopedia.loginregister.login.behaviour.case

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.managepassword.forgotpassword.view.activity.ForgotPasswordActivity
import org.junit.Before
import org.junit.Test

class LoginNormalCase : LoginBase() {

    @Before
    fun defaultResponse() {
        setDefaultDiscover()
    }


    @Test
    /* Show password input field when user click on submit button */
    fun passwordInputField_Showing() {
        setRegisterCheckDefaultResponse()

        launchDefaultFragment()
        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()

        shouldBeDisplayed(R.id.wrapper_password)
        isDisplayingGivenText(R.id.register_btn, "Masuk")
    }

    @Test
    /* Hide password input field when user click on Ubah button */
    fun passwordInputField_Hidden() {
        setRegisterCheckDefaultResponse()

        launchDefaultFragment()
        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()
        clickUbahButton()

        shouldBeHidden(R.id.wrapper_password)
        isDisplayingGivenText(R.id.register_btn, "Selanjutnya")
    }

    @Test
    /* Show Discover Bottom Sheet when user click on Metode Lain button */
    fun showSocialMediaBottomSheet_True() {
        setRegisterCheckDefaultResponse()
        launchDefaultFragment()

        clickSocmedButton()
        shouldBeDisplayed(R.id.socmed_container)
    }

    @Test
    fun showNotRegisteredDialog_IfEmailNotRegistered() {
        val data = RegisterCheckData(isExist = false, userID = "0", registerType = "email", view = "yoris.prayogo@tokopedia.com")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)
        launchDefaultFragment()

        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()

        isDialogDisplayed("Email Belum Terdaftar")
    }

    @Test
    /* Check if RegisterInitialActivity is launching when Daftar button clicked */
    fun goToRegisterInitial_Toolbar() {
        launchDefaultFragment()
        clickTopRegister()
        intended(hasComponent(RegisterInitialActivity::class.java.name))
    }

    @Test
    /* Check if ForgotPasswordActivity is launching when Daftar button clicked */
    fun openTokopediaCarePage() {
        launchDefaultFragment()
        clickForgotPass()
        intended(hasComponent(ForgotPasswordActivity::class.java.name))
    }
}