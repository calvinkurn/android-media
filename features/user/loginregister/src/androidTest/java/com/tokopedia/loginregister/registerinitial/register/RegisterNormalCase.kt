package com.tokopedia.loginregister.registerinitial.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.registerinitial.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.utils.respondWithOk
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
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
            intending(hasComponent(LoginActivity::class.java.name)).respondWithOk()
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Sudah Terdaftar")

            Espresso.onView(ViewMatchers.withText("Ya, Masuk"))
                .inRoot(RootMatchers.isDialog())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())

            intended(hasComponent(LoginActivity::class.java.name))
        }
    }

    @Test
    /* Go to verification page if email not exist */
    fun gotoVerificationpage_IfNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = false , userID = "123456", registerType = "email", view = "yoris.prayogo+3@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()
            inputEmailOrPhone("yoris.prayogo+3@tokopedia.com")
            clickSubmit()
            intended(hasData(ApplinkConstInternalUserPlatform.COTP))
        }
    }

    @Test
    /* Check if LoginActivity is launching when Top Masuk button clicked */
    fun goToLogin_Top() {
        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.LOGIN)).respondWithOk()
            clickTopLogin()
            intended(hasData(ApplinkConstInternalUserPlatform.LOGIN))
        }
    }

    @Test
    /* Show Discover Bottom Sheet when user click on Metode Lain button */
    fun showSocialMediaBottomSheet_True() {
        runTest {
            clickSocmedButton()
            shouldBeDisplayed(R.id.providerName)
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
        val data = RegisterCheckData(isExist = false , userID = "0", registerType = "phone", view = "08224245450411")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("08224245450411")
            clickSubmit()
            isDialogDisplayed("08224245450411")
        }
    }

    @Test
    /* Go to login page if registered phone dialog clicked */
    fun gotoLoginIfPhoneDialogClicked() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "phone", view = "082242454511")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")

            Espresso.onView(ViewMatchers.withText("Ya, Masuk"))
                .inRoot(RootMatchers.isDialog())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())

            intended(hasData(ApplinkConstInternalUserPlatform.COTP))
        }
    }

}
