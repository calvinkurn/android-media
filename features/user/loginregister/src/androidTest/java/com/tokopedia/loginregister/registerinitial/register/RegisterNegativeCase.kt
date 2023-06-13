package com.tokopedia.loginregister.registerinitial.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.registerinitial.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.stub.Config
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test

@UiTest
class RegisterNegativeCase: RegisterInitialBase() {

    @Test
    /* Disable button "Selanjutnya" when input text is empty */
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
            isDisplayingSubGivenText(errorMsg)
        }
    }

    @Test
    /* Show snackbar if discover providers is empty */
    fun forbiddenPage_discoverEmpty() {
        fakeGraphqlRepository.discoverConfig = Config.Error
        runTest {
            onView(
                withText(containsString("Terjadi kesalahan. Ulangi beberapa saat lagi"))
            ).check(matches(isDisplayed()))
        }
    }

}
