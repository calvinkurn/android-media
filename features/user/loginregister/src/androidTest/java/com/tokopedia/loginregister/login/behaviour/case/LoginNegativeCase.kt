package com.tokopedia.loginregister.login.behaviour.case

import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.stub.Config
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test

@UiTest
class LoginNegativeCase: LoginBase() {

    @Test
    /* Disable button "Selanjutnya" when input text is empty */
    fun disableNextButton_ifEmpty() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            deleteEmailOrPhoneInput()
            shouldBeDisabled(R.id.register_btn)
        }
    }

    @Test
    /* Disable button "Selanjutnya" when input text is not valid email */
    fun disableNextButton_ifNotValidEmail() {
        runTest {
            inputEmailOrPhone("yoris.prayogo")
            shouldBeDisabled(R.id.register_btn)
        }
    }

    @Test
    /* Disable button "Selanjutnya" when input text length is too short for phone number */
    fun phoneNumberTooShort() {
        runTest {
            inputEmailOrPhone("08224")
            deleteEmailOrPhoneInput()
            shouldBeDisabled(R.id.register_btn)
        }
    }

    @Test
    /* Disable button "Selanjutnya" when input text length is too long for phone number */
    fun phoneNumberTooLong() {
        val errorMsg = "Phone too long"
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("12345678901234567")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    @Test
    /* Got error from backend during register check */
    fun registerCheckError_BE() {
        val errorMsg = "got errors from be"
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("12345678901234567")
            clickSubmit()
            isDisplayingSubGivenText(errorMsg)
        }
    }

    @Test
    /* Show snackbar if discover providers is empty */
    fun forbiddenPage_discoverEmpty() {
        fakeRepo.discoverConfig = Config.Error
        runTest {
            isDisplayingSubGivenText(com.google.android.material.R.id.snackbar_text, "Terjadi kesalahan. Ulangi beberapa saat lagi (1005)")
        }
    }

    @Test
    /* Display error text when user click on Masuk button while password is empty */
    fun displayError_IfPasswordEmpty() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            clickSubmit()
            isDisplayingGivenText("Kata sandi harus diisi")
        }
    }

    @Test
    /* Disable Email Input if password wrapper visible */
    fun disableEmailInput_IfPasswordVisible() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            shouldBeDisabledWithInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
        }
    }

    @Test
    /* Show toaster when got error response from backend during login v2 */
    fun login_errorBE() {
        val errorMsg = "Salah pak"
        val data = LoginToken(errors = arrayListOf(Error("", errorMsg)))
        val loginToken = LoginTokenPojoV2(data)
        loginTokenV2UseCaseStub.response = loginToken

        val keyData = KeyData(key = "abc1234", hash = "1234")
        val keyResponse = GenerateKeyPojo(keyData = keyData)
        generatePublicKeyUseCaseStub.response = keyResponse

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword("test12345678")
            clickSubmit()
        }

        onView(allOf(withText(containsString(errorMsg)), isDisplayed()))
    }

    @Test
    /* Show popup error */
    fun showPopupAkamai() {
        val title = "header title"
        val popupError = PopupError(title, "body", "action")
        val data = LoginToken(popupError = popupError)
        val loginToken = LoginTokenPojoV2(data)
        loginTokenV2UseCaseStub.response = loginToken

        val keyData = KeyData(key = "abc1234", hash = "1234")
        val keyResponse = GenerateKeyPojo(keyData = keyData)
        generatePublicKeyUseCaseStub.response = keyResponse

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword("test12345678")
            clickSubmit()

            isDialogDisplayed(title)
        }
    }

//    @Test
//    /* Show Error if google Login Failed */
//    fun showError_IfLoginGoogleFailed() {
//        runTest {
//            intending(hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
//                    .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
//
//            clickSocmedButton()
//            onView(withText("Google"))
//                    .inRoot(RootMatchers.isDialog())
//                    .check(matches(ViewMatchers.isDisplayed()))
//                    .perform(click())
//
//            isDisplayingGivenText(R.id.snackbar_txt, "Akun gagal terautentikasi (%s). Mohon coba kembali.")
//        }
//    }

}
