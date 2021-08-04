package com.tokopedia.loginregister.login.behaviour.case.register

import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import org.junit.Test

class RegisterNegativeCase: RegisterInitialBase() {

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
    /* Disable button "Selanjutnya" when input text length is too long for email */
    fun emailTooLong() {
        runTest {
            inputEmailOrPhone("yorisprayogooooo@gmail.commmmmmmmmmmmmmmmmmmmmmmmm")
            shouldBeDisabled(R.id.register_btn)
        }
    }

    @Test
    /* Got error from backend during register check */
    fun registerCheckError_BE() {
        val errorMsg = "got errors from be"
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(errors = arrayListOf(errorMsg))
        registerCheckUseCase.response = RegisterCheckPojo(data = data)

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDisplayingGivenText(R.id.tv_error, errorMsg)
        }
    }

    @Test
    /* Show snackbar if discover providers is empty */
    fun forbiddenPage_discoverEmpty() {
        isDefaultDiscover = false
        runTest {
            isDisplayingGivenText(com.google.android.material.R.id.snackbar_text, "Terjadi kesalahan. Ulangi beberapa saat lagi")
        }
    }

}