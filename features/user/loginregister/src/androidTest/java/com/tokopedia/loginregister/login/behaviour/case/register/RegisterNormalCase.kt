package com.tokopedia.loginregister.login.behaviour.case.register

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

}