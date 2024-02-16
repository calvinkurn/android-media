package com.tokopedia.loginregister.registerinitial.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.registerinitial.base.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.utils.respondWithOk
import com.tokopedia.test.application.annotations.UiTest
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Test

@UiTest
class RegisterNormalCase : RegisterInitialBase() {

    /* Test case 2: Go to login page if registered email dialog clicked */
    @Test
    fun testCase2_gotoLogin_IfDialogClicked() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = true,
            userID = "123456",
            registerType = "email",
            view = "yoris.prayogooooo@tokopedia.com"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            intending(hasComponent(LoginActivity::class.java.name)).respondWithOk()
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Sudah Terdaftar")

            onView(withText("Ya, Masuk"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click())

            intended(hasComponent(LoginActivity::class.java.name))
        }
    }

    /* Go to verification page if email not exist */
    @Test
    fun gotoVerificationpage_IfNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = false,
            userID = "123456",
            registerType = "email",
            view = "yoris.prayogo+3@tokopedia.com"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            setupRollence()
            intending(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_TYPE_REGISTER.toString()
                    )
                )
            ).respondWithOk()
            inputEmailOrPhone("yoris.prayogo+3@tokopedia.com")
            clickSubmit()
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
//    fun gotoScpVerificationpage_WhenRollenceCvsdkActive_IfNotRegistered() {
//        isDefaultRegisterCheck = false
//        val data = RegisterCheckData(isExist = false, userID = "123456", registerType = "email", view = "yoris.prayogo+3@tokopedia.com")
//        registerCheckUseCase.response = RegisterCheckPojo(data)
//
//        runTest {
//            mockkStatic(RemoteConfigInstance::class)
//            every {
//                RemoteConfigInstance.getInstance().abTestPlatform.getString(DeeplinkMapperUser.ROLLENCE_CVSDK_INTEGRATION)
//            } returns DeeplinkMapperUser.ROLLENCE_CVSDK_INTEGRATION
//            intending(hasData(UriUtil.buildUri(ApplinkConstInternalUserPlatform.COTP, RegisterConstants.OtpType.OTP_TYPE_REGISTER.toString()))).respondWithOk()
//            inputEmailOrPhone("yoris.prayogo+3@tokopedia.com")
//            clickSubmit()
//            intended(hasData(ApplinkConstInternalUserPlatform.SCP_OTP))
//            unmockkStatic(RemoteConfigInstance::class)
//        }
//    }

    /** automation click on button masuk top right on register page **/
    @Test
    fun goToLogin_TopRight() {
        runTest {
            intending(hasData(ApplinkConstInternalUserPlatform.LOGIN)).respondWithOk()
            clickTopLogin()
            intended(hasData(ApplinkConstInternalUserPlatform.LOGIN))
        }
    }

    /** Show Discover Bottom Sheet when user click on Metode Lain button **/
    @Test
    fun showSocialMediaBottomSheet_True() {
        runTest {
            clickSocmedButton()
            shouldBeDisplayed(R.id.providerName)
        }
    }

    /* Show not registered dialog if phone registered */
    @Test
    fun showNotRegisteredDialog_IfPhoneRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = true,
            userID = "123456",
            registerType = "phone",
            view = "082242454511"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")
        }
    }

    /* Show proceed dialog if phone not registered */
    @Test
    fun showProceedPhoneDialog_IfPhoneNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = false,
            userID = "0",
            registerType = "phone",
            view = "08224245450411"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("08224245450411")
            clickSubmit()
            isDialogDisplayed("08224245450411")
        }
    }

    /* Go to login page if registered phone dialog clicked */
    @Test
    fun gotoLogin_IfPhoneDialogClicked() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = true,
            userID = "123456",
            registerType = "phone",
            view = "082242454511"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            setupRollence()
            intending(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_LOGIN_PHONE_NUMBER.toString()
                    ).toString()
                )
            ).respondWithOk()
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")

            onView(withText("Ya, Masuk"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click())

            intended(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_LOGIN_PHONE_NUMBER.toString()
                    )
                )
            )
        }
    }
}
