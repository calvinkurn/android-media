package com.tokopedia.loginregister.registerinitial.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.base.RegisterInitialBase
import com.tokopedia.loginregister.stub.Config
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test

@UiTest
class RegisterInitialNegativeCase: RegisterInitialBase() {

    /* Disable button "Selanjutnya" when input text is empty */
    @Test
    fun whenInvalidInput_registerButtonIsDisabled() {
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            onView(withId(R.id.register_btn)).check(matches(isEnabled()))

            // empty
            deleteEmailOrPhoneInput()
            shouldBeDisabled(R.id.register_btn)

            // invalid email/phone
            inputEmailOrPhone("yoris.prayogo")
            shouldBeDisabled(R.id.register_btn)

            // long text
            inputEmailOrPhone("yorisprayogooooo@gmail.commmmmmmmmmmmmmmmmmmmmmmmm")
            shouldBeDisabled(R.id.register_btn)
        }
    }

    /* Disable button "Daftar" when input text length is too long for phone number */
    @Test
    fun showError_ifPhoneNumberTooLong() {
        val errorMsg = "Nomor ponsel terlalu panjang, maksimum 15 angka."
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("08123456789012345678")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    /* Disable button "Daftar" when input text length is too short for phone number */
    @Test
    fun showError_ifPhoneNumberTooShort() {
        val errorMsg = "Nomor ponsel terlalu pendek, minimum 8 angka."
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("0812342")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    /* Disable button "Selanjutnya" when input text is blacklisted email */
    @Test
    fun showError_ifEmailIsBlacklisted() {
        val errorMsg = "Alamat email tidak dapat digunakan."
        val data = RegisterCheckPojo(
            RegisterCheckData(
                errors = arrayListOf(
                    errorMsg
                )
            )
        )
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("kim.mingyu@seventeen.com")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    /* Disable button "Daftar" when input text length is blacklisted phone number */
    @Test
    fun showError_ifPhoneNumberIsBlacklisted() {
        val errorMsg = "Nomor handphone ini tidak dapat digunakan."
        val data = RegisterCheckPojo(RegisterCheckData(errors = arrayListOf(errorMsg)))
        fakeRepo.registerCheckConfig = Config.WithResponse(data)

        runTest {
            inputEmailOrPhone("081234224323")
            clickSubmit()
            isDisplayingGivenText(errorMsg)
        }
    }

    @Test
    /* Got error from backend during register check */
    fun registerCheckError_BE() {
        val errorMsg = "got errors from be"
        val data = RegisterCheckData(errors = arrayListOf(errorMsg))
        fakeRepo.registerCheckConfig = Config.WithResponse(RegisterCheckPojo(data = data))

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDisplayingSubGivenText(errorMsg)
        }
    }

    /* Show snackbar if discover providers is empty */
    @Test
    fun forbiddenPage_discoverEmpty() {
        fakeRepo.discoverConfig = Config.Error
        runTest {
            onView(
                withText(containsString("Terjadi kesalahan. Ulangi beberapa saat lagi"))
            ).check(matches(isDisplayed()))
        }
    }

}
