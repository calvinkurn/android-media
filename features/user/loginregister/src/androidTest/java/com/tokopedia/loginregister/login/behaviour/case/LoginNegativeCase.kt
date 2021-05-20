package com.tokopedia.loginregister.login.behaviour.case

import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
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

}