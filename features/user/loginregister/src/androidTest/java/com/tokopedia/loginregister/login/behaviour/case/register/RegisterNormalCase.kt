package com.tokopedia.loginregister.login.behaviour.case.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.login.behaviour.activity.VerificationActivityStub
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import org.junit.Test

class RegisterNormalCase: RegisterInitialBase() {

    @Test
    /* Show Go to login dialog if email exist */
    fun gotoLoginIfEmailExist() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Sudah Terdaftar")
        }
    }

    @Test
    /* Go to login page if registered email dialog clicked */
    fun gotoLoginIfDialogClicked() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Sudah Terdaftar")

            Espresso.onView(ViewMatchers.withText("Ya, Masuk"))
                .inRoot(RootMatchers.isDialog())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())

            Intents.intended(IntentMatchers.hasComponent(LoginActivityStub::class.java.name))
        }
    }

    @Test
    /* Go to verification page if email not exist */
    fun gotoVerificationpage_IfNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = false , userID = "123456", registerType = "email", view = "yoris.prayogo+3@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("yoris.prayogo+3@tokopedia.com")
            clickSubmit()
            Intents.intended(IntentMatchers.hasComponent(VerificationActivityStub::class.java.name))
        }
    }

    @Test
    /* Check if LoginActivity is launching when Top Masuk button clicked */
    fun goToLogin_Top() {
        runTest {
            clickTopLogin()
            Intents.intended(IntentMatchers.hasComponent(LoginActivityStub::class.java.name))
        }
    }

    @Test
    /* Show Discover Bottom Sheet when user click on Metode Lain button */
    fun showSocialMediaBottomSheet_True() {
        runTest {
            clickSocmedButton()
            shouldBeDisplayed(R.id.socmed_container)
        }
    }

    @Test
    /* Show not registered dialog if phone registered */
    fun showNotRegisteredDialog_IfPhoneRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "phone", view = "082242454511")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")
        }
    }

    @Test
    /* Show proceed dialog if phone not registered */
    fun showProceedPhoneDialog_IfPhoneNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = false , userID = "0", registerType = "phone", view = "082242454511")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("0822-4245-4504-11")
        }
    }

    @Test
    /* Go to login page if registered phone dialog clicked */
    fun gotoLoginIfPhoneDialogClicked() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "phone", view = "082242454511")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")

            Espresso.onView(ViewMatchers.withText("Ya, Masuk"))
                .inRoot(RootMatchers.isDialog())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())

            Intents.intended(IntentMatchers.hasComponent(LoginActivityStub::class.java.name))
        }
    }

}