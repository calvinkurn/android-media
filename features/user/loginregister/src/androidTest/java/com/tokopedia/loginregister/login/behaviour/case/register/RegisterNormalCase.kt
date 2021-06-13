package com.tokopedia.loginregister.login.behaviour.case.register

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import org.junit.Test

class RegisterNormalCase: RegisterInitialBase() {

    @Test
    /* Show Go to login dialog if email exist */
    fun gotoLoginIfEmailExist() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "email", view = "yoris.prayogooooo@tokopedia.com")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            isDialogDisplayed("Email Sudah Terdaftar")
        }
    }

    @Test
    /* Check if RegisterInitialActivity is launching when Top Daftar button clicked */
    fun goToRegisterInitial_Top() {
        runTest {
            clickTopLogin()
            Intents.intended(IntentMatchers.hasComponent(LoginActivityStub::class.java.name))
        }
    }

    @Test
    /* Show Discover Bottom Sheet when user click on Metode Lain button */
    fun showSocialMediaBottomSheet_True() {
        runTest {
            clickSocmedButton()
            shouldBeDisplayed(R.id.socmed_container)
        }
    }

    @Test
    /* Show not registered dialog if phone registered */
    fun showNotRegisteredDialog_IfPhoneRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = true , userID = "123456", registerType = "phone", view = "082242454511")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("Nomor Ponsel Sudah Terdaftar")
        }
    }

    @Test
    /* Show proceed dialog if phone not registered */
    fun showProceedPhoneDialog_IfPhoneNotRegistered() {
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(isExist = false , userID = "0", registerType = "phone", view = "082242454511")
        registerCheckUseCase.response = RegisterCheckPojo(data)

        runTest {
            inputEmailOrPhone("082242454511")
            clickSubmit()
            isDialogDisplayed("0822-4245-4504-11")
        }
    }

}