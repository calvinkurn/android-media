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
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.loginregister.stub.Config
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertTrue
import org.junit.Test

@UiTest
class LoginNormalCase : LoginBase() {

    /* Test Case 1: Check if activity is finished when login success */
    @Test
    fun testCase1_finishActivityIfLoginSuccess() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = true,
                userID = "7891022",
                registerType = "email",
                view = "kim.mingyu@tokopedia.com",
                isPending = false
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        val loginToken = LoginToken(accessToken = "abc123")
        val loginPojo = LoginTokenPojo(loginToken)
        loginTokenUseCaseStub.response = loginPojo

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword("test123")
            clickSubmit()
            assertTrue(activityTestRule.activity.isFinishing)
        }
    }

    /* Test Case 2: Go to verification page if phone exist */
    @Test
    fun testCase2_gotoVerificationFragment_IfPhoneExist() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = true,
                userID = "123456",
                registerType = "phone",
                view = "082242454504"
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    Intent()
                )
            )
            inputEmailOrPhone("082242454504")
            clickSubmit()
            intended(hasData(ApplinkConstInternalUserPlatform.COTP))
        }
    }

    /* Show password input field when user click on submit button */
    @Test
    fun passwordInputField_Showing() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()

            shouldBeDisplayed(R.id.wrapper_password)
            isDisplayingGivenText(R.id.register_btn, "Masuk")
        }
    }

    /* Test case 12: Hide password input field when user click on Ubah button */
    @Test
    fun testCase12_passwordInputField_Hidden() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            clickUbahButton()

            shouldBeHidden(R.id.wrapper_password)
            isDisplayingGivenText(R.id.register_btn, "Selanjutnya")
        }
    }

    /* Show Discover Bottom Sheet when user click on Metode Lain button */
    @Test
    fun showSocialMediaBottomSheet_True() {
        runTest {
            clickSocmedButton()
            shouldBeDisplayed(R.id.providerName)
        }
    }

    /* Check if RegisterInitialActivity is launching when Top Daftar button clicked */
    @Test
    fun goToRegisterInitial_Top() {
        runTest {
            Thread.sleep(1000)

            clickTopRegister()
            intended(hasComponent(RegisterInitialActivity::class.java.name))
        }
    }

    /* Check if RegisterInitialActivity is launching when Bottom Daftar button clicked */
    @Test
    fun goToRegisterInitial_Bottom() {
        runTest {
            clickBottomRegister()
            intended(hasComponent(RegisterInitialActivity::class.java.name))
        }
    }

    /**
     * Test Case 8: Check if RegisterInitialActivity is launching
     * when Daftar button in dialog clicked
     * */
    @Test
    fun testCase8_goToRegisterInitial_IfNotRegistered() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = false,
                userID = "0",
                registerType = "email",
                view = "yoris.prayogo@tokopedia.com"
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            mockOtpPageRegisterEmail()
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()

            isDialogDisplayed("Email Belum Terdaftar")

            onView(withText("Ya, Daftar"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click())

            intended(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_TYPE_REGISTER.toString()
                    )
                )
            )
        }
    }

//    @Deprecated("SCP code need to be removed")
//    @Test
//    fun goToRegisterInitial_IfNotRegistered_WhenRollenceScpCvsdkActive() {
//        val data = RegisterCheckPojo(
//            RegisterCheckData(
//                isExist = false,
//                userID = "0",
//                registerType = "email",
//                view = "yoris.prayogo@tokopedia.com"
//            )
//        )
//        fakeRepo.registerCheckConfig = Config.WithResponse(data)
//
//        runTest {
//            setupRollence(isScpActive = true)
//            mockOtpPageRegisterEmail()
//            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
//            clickSubmit()
//
//            onView(withText("Ya, Daftar"))
//                .inRoot(isDialog())
//                .check(matches(isDisplayed()))
//                .perform(click())
//
//            intended(hasData(ApplinkConstInternalUserPlatform.SCP_OTP))
//        }
//    }

    /**
     * Success redirection to tokopedia://addname if user able to login
     */
    @Test
    fun gotoChangeName_IfLoginSuccess() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = true,
                userID = "123456",
                registerType = "email",
                view = "yoris.prayogo@tokopedia.com",
                isPending = false
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        val loginToken = LoginToken(accessToken = "abc123")
        val loginPojo = LoginTokenPojo(loginToken)
        loginTokenUseCaseStub.response = loginPojo

        val profileInfo = ProfileInfo(userId = "123456", fullName = "CHARACTER_NOT_ALLOWED")
        val profilePojo = ProfilePojo(profileInfo)
        getProfileUseCaseStub.response = profilePojo

        runTest {
            mockAddNameProfilePage()
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword("test123")
            clickSubmit()
            intended(hasData(ApplinkConst.ADD_NAME_PROFILE))
        }
    }

    /**
     * test case forgot password applink
     */
    @Test
    fun whenForgotPasswordIsClicked_TheApplinkPageIsLaunched() {
        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
            )
            clickForgotPass()
            intended(hasData(ApplinkConstInternalUserPlatform.FORGOT_PASSWORD))
        }
    }

    /**
     * test case inactive phone number applink
     */
    @Test
    fun whenInactivePhoneNumberIsClicked_TheApplinkPageIsLaunched() {
        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER)).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
            )
            clickInactivePhoneNumber()
            intended(hasData(ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER))
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
        isDisplayingGivenText("yoris.prayogo")
        isEmailExtensionDismissed()
        inputEmailOrPhone("@")
        isEmailExtensionDisplayed()
    }

    @Test
    fun checkEmailExtensionAdded() {
        checkEmailExtensionShownAfterAddAt()

        onView(withId(R.id.recyclerViewEmailExtension))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<EmailExtensionAdapter.ViewHolder>(
                        0,
                        clickOnViewChild(R.id.textEmailExtension)
                    )
            )
        isDisplayingGivenText("yoris.prayogo@gmail.com")
    }

    @Test
    fun checkEmailExtensionAdded2() {
        checkEmailExtensionShownAfterAddAt()
        onView(withId(R.id.recyclerViewEmailExtension)).perform(
            scrollToPosition<EmailExtensionAdapter.ViewHolder>(
                6
            )
        )
        onView(withId(R.id.recyclerViewEmailExtension))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<EmailExtensionAdapter.ViewHolder>(
                        6,
                        clickOnViewChild(R.id.textEmailExtension)
                    )
            )
        isDisplayingGivenText("yoris.prayogo@outlook.com")
    }

    @Test
    fun checkEmailExtensionDismissedAfterAdded() {
        checkEmailExtensionAdded()
        isEmailExtensionDismissed()
    }

    @Test
    fun checkDeveloperOptions() {
        runTest {
            Thread.sleep(1000)

            val viewDevOpts = onView(withText("Developer Options"))
            if (GlobalConfig.isAllowDebuggingTools()) {
                viewDevOpts.check(matches(isDisplayed()))
            } else {
                viewDevOpts.check(matches(not(isDisplayed())))
            }
        }
    }

//    @Deprecated("SCP code need to be removed")
//    @Test
//    fun gotoVerification_true() {
//        runTest {
//            Thread.sleep(1000)
//
//            val viewDevOpts = onView(withText("Developer Options"))
//            if (GlobalConfig.isAllowDebuggingTools()) {
//                viewDevOpts.check(matches(isDisplayed()))
//            } else {
//                viewDevOpts.check(matches(not(isDisplayed())))
//            }
//        }
//    }

    private fun mockAddNameProfilePage() {
        intending(hasData(ApplinkConst.ADD_NAME_PROFILE)).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtras(
                        Bundle().apply {
                            putString(ApplinkConstInternalGlobal.PARAM_UUID, "abc1234")
                            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, "abv1234")
                            putString(
                                ApplinkConstInternalGlobal.PARAM_EMAIL,
                                "yoris.prayogo@gmail.com"
                            )
                        }
                    )
                }
            )
        )
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById<View>(viewId))
    }
}
