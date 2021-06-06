package com.tokopedia.loginregister.login.behaviour.case

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.loginregister.login.behaviour.activity.ChangeNameActivityStub
import com.tokopedia.loginregister.login.behaviour.activity.ChooseAccountActivityStub
import com.tokopedia.loginregister.login.behaviour.activity.VerificationActivityStub
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterEmailActivity
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.managepassword.forgotpassword.view.activity.ForgotPasswordActivity
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import org.hamcrest.CoreMatchers.not
import org.junit.Test


class LoginNormalCase : LoginBase() {

    @Test
    /* Go to verification page if phone exist */
    fun gotoVerificationFragment_IfPhoneExist() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true, userID = "123456", registerType = "phone", view = "082242454504")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)

        runTest {
            inputEmailOrPhone("082242454504")
            clickSubmit()
            intended(hasComponent(VerificationActivityStub::class.java.name))
        }
    }

    @Test
    /* Show password input field when user click on submit button */
    fun passwordInputField_Showing() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()

            shouldBeDisplayed(R.id.wrapper_password)
            isDisplayingGivenText(R.id.register_btn, "Masuk")
        }
    }

    @Test
    /* Hide password input field when user click on Ubah button */
    fun passwordInputField_Hidden() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            clickUbahButton()

            shouldBeHidden(R.id.wrapper_password)
            isDisplayingGivenText(R.id.register_btn, "Selanjutnya")
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
    /* Show not registered dialog if email not registered */
    fun showNotRegisteredDialog_IfEmailNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = false, userID = "0", registerType = "email", view = "yoris.prayogo@tokopedia.com")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Belum Terdaftar")
        }
    }

    @Test
    /* Check if RegisterInitialActivity is launching when Top Daftar button clicked */
    fun goToRegisterInitial_Top() {
        runTest {
            clickTopRegister()
            intended(hasComponent(RegisterInitialActivity::class.java.name))
        }
    }

    @Test
    /* Check if RegisterInitialActivity is launching when Bottom Daftar button clicked */
    fun goToRegisterInitial_Bottom() {
        runTest {
            clickBottomRegister()
            intended(hasComponent(RegisterInitialActivity::class.java.name))
        }
    }

    fun mockVerificationSuccess() {
        val mockVerificationResult = Intent().apply {
             putExtras(Bundle().apply {
                putString(ApplinkConstInternalGlobal.PARAM_UUID, "abc1234")
                putString(ApplinkConstInternalGlobal.PARAM_TOKEN, "abv1234")
                putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "yoris.prayogo@gmail.com")
            })
        }
        intending(hasComponent(VerificationActivityStub::class.java.name)).respondWith(Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                mockVerificationResult
        ))
    }

    private fun mockChooseAccountSuccess() {
//        val mockVerificationResult = Intent().apply {
//            putExtras(Bundle().apply {
//                putString(ApplinkConstInternalGlobal.PARAM_UUID, "abc1234")
//                putString(ApplinkConstInternalGlobal.PARAM_TOKEN, "abv1234")
//                putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "yoris.prayogo@gmail.com")
//            })
//        }
        intending(hasComponent(ChooseAccountActivityStub::class.java.name)).respondWith(Instrumentation.ActivityResult(
            Activity.RESULT_OK,
            null
        ))
    }

    @Test
    /* Check if RegisterInitialActivity is launching when Daftar button in dialog clicked */
    fun goToRegisterInitial_IfNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = false, userID = "0", registerType = "email", view = "yoris.prayogo@tokopedia.com")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)

        runTest {
            mockVerificationSuccess()
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()

            onView(withText("Ya, Daftar"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click())
            intended(hasComponent(RegisterInitialActivity::class.java.name))
            intended(hasComponent(RegisterEmailActivity::class.java.name))
        }
    }

    @Test
    fun gotoChangeNameIfLoginSuccess() {
        isDefaultRegisterCheck = false
        val regCheck = RegisterCheckData(isExist = true, userID = "123456", registerType = "email", view = "yoris.prayogo@tokopedia.com", isPending = false)
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = regCheck)

        val loginToken = LoginToken(accessToken = "abc123")
        val loginPojo = LoginTokenPojo(loginToken)
        loginTokenUseCaseStub.response = loginPojo

        val keyData = KeyData(key = "abc1234", hash = "1234")
        val keyResponse = GenerateKeyPojo(keyData = keyData)
        generatePublicKeyUseCaseStub.response = keyResponse

        val profileInfo = ProfileInfo(userId = "123456", fullName = "CHARACTER_NOT_ALLOWED")
        val profilePojo = ProfilePojo(profileInfo)
        getProfileUseCaseStub.response = profilePojo


        runTest {
            mockVerificationSuccess()
            mockChooseAccountSuccess()

            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword("test123")
            clickSubmit()
            intended(hasComponent(ChangeNameActivityStub::class.java.name))
        }
    }

    @Test
    /* Check if ForgotPasswordActivity is launching when Daftar button clicked */
    fun openTokopediaCarePage() {
        runTest {
            clickForgotPass()
            intended(hasComponent(ForgotPasswordActivity::class.java.name))
        }
    }


    @Test
    fun checkEmailExtensionShownAfterAddAt() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@")
            isEmailExtensionDisplayed()
        }

    }

    @Test
    fun checkEmailExtensionVisibility() {
        checkEmailExtensionShownAfterAddAt()
        onView(withId(R.id.input_email_phone))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
        isDisplayingGivenText(R.id.input_email_phone, "yoris.prayogo")
        isEmailExtensionDismissed()
        inputEmailOrPhone("@")
        isEmailExtensionDisplayed()
    }


    @Test
    fun checkEmailExtensionAdded() {
        checkEmailExtensionShownAfterAddAt()

        onView(withId(R.id.recyclerViewEmailExtension))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<EmailExtensionAdapter.ViewHolder>(0, clickOnViewChild(R.id.textEmailExtension)))
        isDisplayingGivenText(R.id.input_email_phone, "yoris.prayogo@gmail.com")
    }

    @Test
    fun checkEmailExtensionAdded2() {
        checkEmailExtensionShownAfterAddAt()
        onView(withId(R.id.recyclerViewEmailExtension)).perform(scrollToPosition<EmailExtensionAdapter.ViewHolder>(6))
        onView(withId(R.id.recyclerViewEmailExtension))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<EmailExtensionAdapter.ViewHolder>(6, clickOnViewChild(R.id.textEmailExtension)))
        isDisplayingGivenText(R.id.input_email_phone, "yoris.prayogo@outlook.com")
    }

    @Test
    fun checkEmailExtensionDismissedAfterAdded() {
        checkEmailExtensionAdded()
        isEmailExtensionDismissed()
    }

    @Test
    fun checkDeveloperOptions() {
        runTest {
            val viewDevOpts = onView(withText("Developer Options"))
            if (GlobalConfig.isAllowDebuggingTools()) {
                viewDevOpts.check(matches(isDisplayed()))
            } else {
                viewDevOpts.check(matches(not(isDisplayed())))
            }
        }
    }

    @Test
    fun gotoVerification_true() {
        runTest {
            val viewDevOpts = onView(withText("Developer Options"))
            if (GlobalConfig.isAllowDebuggingTools()) {
                viewDevOpts.check(matches(isDisplayed()))
            } else {
                viewDevOpts.check(matches(not(isDisplayed())))
            }
        }
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }
}
