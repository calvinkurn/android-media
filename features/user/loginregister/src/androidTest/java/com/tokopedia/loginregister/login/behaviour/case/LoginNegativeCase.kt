package com.tokopedia.loginregister.login.behaviour.case

import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.sessioncommon.data.*
import org.junit.Test

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
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(errors = arrayListOf(errorMsg))
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)

        runTest {
            inputEmailOrPhone("12345678901234567")
            clickSubmit()
            isDisplayingGivenText(R.id.tv_error, errorMsg)
        }
    }

    @Test
    /* Got error from backend during register check */
    fun registerCheckError_BE() {
        val errorMsg = "got errors from be"
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(errors = arrayListOf(errorMsg))
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)

        runTest {
            inputEmailOrPhone("12345678901234567")
            clickSubmit()
            isDisplayingGivenText(R.id.tv_error, errorMsg)
        }
    }

    @Test
    /* Show snackbar if discover providers is empty */
    fun forbiddenPage_discoverEmpty() {
        isDefaultDiscover = false
        runTest {
            isDisplayingGivenText(com.google.android.material.R.id.snackbar_text, "Terjadi kesalahan. Ulangi beberapa saat lagi (1005)")
        }
    }

    @Test
    /* Display error text when user click on Masuk button while password is empty */
    fun displayError_IfPasswordEmpty() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            clickSubmit()
            isDisplayingGivenText(R.id.textinput_helper_text, "Kata sandi harus diisi")
        }
    }

    @Test
    /* Disable Email Input if password wrapper visible */
    fun disableEmailInput_IfPasswordVisible() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            shouldBeDisabled(R.id.input_email_phone)
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

            isDisplayingGivenText(com.google.android.material.R.id.snackbar_text, errorMsg)
        }
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