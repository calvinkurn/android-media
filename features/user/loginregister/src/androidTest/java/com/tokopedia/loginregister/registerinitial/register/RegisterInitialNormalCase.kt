package com.tokopedia.loginregister.registerinitial.register

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.registerinitial.base.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.stub.Config
import com.tokopedia.loginregister.utils.respondWithOk
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class RegisterInitialNormalCase : RegisterInitialBase() {

    /* Go to login page if email registered already */
    @Test
    fun gotoLogin_IfEmailRegistered() {
        val data = RegisterCheckData(
            isExist = true,
            userID = "123456",
            registerType = "email",
            view = "yoris.prayogooooo@tokopedia.com"
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(RegisterCheckPojo(data))

        runTest {
            intending(hasComponent(LoginActivity::class.java.name)).respondWithOk()
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Sudah Terdaftar")
            clickOnDialog("Ya, Masuk")

            intended(hasComponent(LoginActivity::class.java.name))
        }
    }

    /* Go to OTP page if email not exist */
    @Test
    fun gotoOtpPage_IfEmailIsNotRegistered() {
        val data = RegisterCheckData(
            isExist = false,
            userID = "123456",
            registerType = "email",
            view = "yoris.prayogo+3@tokopedia.com"
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(RegisterCheckPojo(data))

        runTest {
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

    /* Show proceed dialog if phone not registered */
    @Test
    fun gotoAddNamePage_IfPhoneNotRegistered() {
        val data = RegisterCheckData(
            isExist = false,
            userID = "0",
            registerType = "phone",
            view = "08224245450411"
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(RegisterCheckPojo(data))

        runTest {
            intending(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_REGISTER_PHONE_NUMBER.toString()
                    )
                )
            ).respondWithOk()

            //redirect user to add register name on profile completion page
            intending(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.ADD_NAME_REGISTER
                    )
                )
            ).respondWithOk()

            inputEmailOrPhone("08224245450411")
            clickSubmit()
            isDialogDisplayed("08224245450411")
            clickOnDialog("Ya, Benar")

            intended(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_REGISTER_PHONE_NUMBER.toString()
                    )
                )
            )

            intended(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.ADD_NAME_REGISTER
                    )
                )
            )
        }
    }

    /* Go to login page if phone number registered already */
    @Test
    fun gotoOtpPage_IfPhoneNumberRegistered() {
        val data = RegisterCheckData(
            isExist = true,
            userID = "123456",
            registerType = "phone",
            view = "082242454511"
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(RegisterCheckPojo(data))

        runTest {
            intending(
                hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.COTP,
                        RegisterConstants.OtpType.OTP_LOGIN_PHONE_NUMBER.toString()
                    )
                )
            ).respondWithOk()
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")
            clickOnDialog("Ya, Masuk")

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
