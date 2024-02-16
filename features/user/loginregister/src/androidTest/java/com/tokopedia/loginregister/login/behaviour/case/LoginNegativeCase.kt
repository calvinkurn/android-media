package com.tokopedia.loginregister.login.behaviour.case

import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.stub.Config
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test

@UiTest
class LoginNegativeCase : LoginBase() {

    /* Test Case 5: Login with invalid format email
    *  Test Case 6: Login with empty email */
    @Test
    fun testCase5And6_givenInputCases_registerButtonShouldDisabled() {
        runTest {
            /* when input text is empty */
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            deleteEmailOrPhoneInput()
            shouldBeDisabled(R.id.register_btn)

            /* input text is not valid email */
            inputEmailOrPhone("yoris.prayogo")
            shouldBeDisabled(R.id.register_btn)

            /* text length is too short for phone number */
            inputEmailOrPhone("08224")
            shouldBeDisabled(R.id.register_btn)

            /* text length is not valid for phone number */
            inputEmailOrPhone("08224asdadsad")
            shouldBeDisabled(R.id.register_btn)
        }
    }

    /* Disable button "Selanjutnya" when input text length is too long for phone number */
    @Test
    fun showError_ifPhoneNumberTooLong() {
        val errorMsg = "Nomor Handphone terlalu panjang, maksimum 15 angka."
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("12345678901234567")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    /* Disable button "Selanjutnya" when input text length is too short for phone number */
    @Test
    fun showError_ifPhoneNumberTooShort() {
        val errorMsg = "Nomor Handphone terlalu pendek, minimum 8 angka."
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("0812342")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    /* Disable button "Selanjutnya" when input text length is blacklisted phone number */
    @Test
    fun showError_ifPhoneNumberIsBlacklisted() {
        val errorMsg = "Nomor ponsel ini tidak dapat digunakan."
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("081234224323")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    /* Got error from backend during register check */
    @Test
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

    /* Show snackbar if discover providers is empty */
    @Test
    fun forbiddenPage_discoverEmpty() {
        fakeRepo.discoverConfig = Config.Error
        runTest {
            isDisplayingSubGivenText(
                com.google.android.material.R.id.snackbar_text,
                "Terjadi kesalahan. Ulangi beberapa saat lagi"
            )
        }
    }

    /* Test Case 3: Display error text when user click on Masuk button while password is empty */
    @Test
    fun testCase3_displayError_IfPasswordEmpty() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            clickSubmit()
            isDisplayingGivenText("Kata sandi harus diisi")
        }
    }

    /* Test Case 4: Display error text when user click on Masuk button while password is wrong */
    @Test
    fun testCase4_displayError_IfPasswordWrong() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = true,
                userID = "123456",
                registerType = "email",
                view = "kim.mingyu@tokopedia.com",
                isPending = false
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        val errors = ArrayList<Error>()
        val errorMessage = "Email atau kata sandi salah"
        errors.add(Error("", errorMessage))
        val loginToken = LoginToken(errors = errors, eventCode = "ErrorBadCredentials")
        val loginPojo = LoginTokenPojo(loginToken)
        loginTokenUseCaseStub.response = loginPojo

        runTest {
            inputEmailOrPhone("kim.mingyu@tokopedia.com")
            clickSubmit()
            inputPassword("test123")
            clickSubmit()
            isDisplayingSubstringGivenText(errorMessage)
        }
    }

    /* Test Case 7: Display error text when user click on Masuk button while email banned */
    @Test
    fun testCase7_displayError_IfAccountBanned() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = true,
                userID = "123456",
                registerType = "email",
                view = "kim.mingyu@tokopedia.com",
                isPending = false
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        val errors = ArrayList<Error>()
        val errorMessage = "Akun ini telah di blokir. Hubungi pusat bantuan untuk info lebih lanjut"
        errors.add(Error("", errorMessage))
        val loginToken = LoginToken(errors = errors, eventCode = "ErrorBadCredentials")
        val loginPojo = LoginTokenPojo(loginToken)
        loginTokenUseCaseStub.response = loginPojo

        runTest {
            inputEmailOrPhone("kim.mingyu@tokopedia.com")
            clickSubmit()
            inputPassword("test123")
            clickSubmit()
            isDisplayingSubstringGivenText(errorMessage)
        }
    }

    /* Test Case 10: Display error text when user click on Masuk button while email status pending */
    @Test
    fun testCase10_displayError_IfEmailPending() {
        val errorList = ArrayList<String>()
        val errorMessage = "Akun Anda belum diaktivasi. Cek email Anda untuk mengaktivasi akun."
        errorList.add(errorMessage)
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = false,
                userID = "0",
                registerType = "",
                view = "",
                status = 0, //looks like unused in the login flow
                isPending = false,
                errors = errorList
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("kim.mingyu@tokopedia.com")
            clickSubmit()
            isDisplayingGivenText(errorMessage)
        }
    }

    /* Test Case 11: Display error text when user click on Masuk button while email status recovery */
    @Test
    fun testCase11_displayError_IfEmailRecovery() {
        val data = RegisterCheckPojo(
            RegisterCheckData(
                isExist = true,
                userID = "0",
                registerType = "email",
                view = "kim.mingyu@tokopedia.com",
                status = 3, //looks like unused in the login flow
                isPending = false
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        val errors = ArrayList<Error>()
        val errorMessage =
            "Akun Anda berada dalam status pengawasan. Demi keamanan, mohon masuk dengan nomor ponsel"
        errors.add(Error("", errorMessage))
        val loginToken = LoginToken(errors = errors, eventCode = "ErrorBadCredentials")
        val loginPojo = LoginTokenPojo(loginToken)
        loginTokenUseCaseStub.response = loginPojo

        runTest {
            inputEmailOrPhone("kim.mingyu@tokopedia.com")
            clickSubmit()
            inputPassword("test123")
            clickSubmit()
            isDisplayingSubstringGivenText(errorMessage)
        }
    }

    /* Disable Email Input if password wrapper visible */
    @Test
    fun disableEmailInput_IfPasswordVisible() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            shouldBeDisabledWithInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
        }
    }

    /* Show toaster when got error response from backend during login v2 */
    @Test
    fun login_errorBE() {
        val errorMsg = "Salah pak"
        val data = LoginToken(errors = arrayListOf(Error("", errorMsg)))
        val loginToken = LoginTokenPojoV2(data)
        loginTokenV2UseCaseStub.response = loginToken

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword("test12345678")
            clickSubmit()
        }

        onView(allOf(withText(containsString(errorMsg)), isDisplayed()))
    }

    /* Show popup error */
    @Test
    fun showPopupAkamai() {
        val title = "header title"
        val popupError = PopupError(title, "body", "action")
        val data = LoginToken(popupError = popupError)
        val loginToken = LoginTokenPojoV2(data)
        loginTokenV2UseCaseStub.response = loginToken

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
//            Intents.intending(IntentMatchers.hasComponent("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
//                    .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
//
//            clickSocmedButton()
//            onView(withText("Google"))
//                    .inRoot(RootMatchers.isDialog())
//                    .check(ViewAssertions.matches(isDisplayed()))
//                    .perform(ViewActions.click())
//
//            isDisplayingSubstringGivenText("Akun gagal terautentikasi (%s). Mohon coba kembali.")
//        }
//    }
}
