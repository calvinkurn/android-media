package com.tokopedia.updateinactivephone.features.inputoldphonenumber

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.checkErrorMessageOnInputPhone
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.setPhoneNumberText
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class InputOldPhoneNumberNotRegisteredTest : BaseInputOldPhoneNumberTest() {

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            inputOldPhoneNumberUseCaseStub.response = registerCheckNotRegisteredModel
        }
    }

    @Test
    fun input_not_registered_number_then_success() {
        //GIVEN
        val phone = "012345678910"

        runTest {
            //WHEN
            setPhoneNumberText(phone)
            clickOnButtonSubmit()

            //THEN
            checkErrorMessageOnInputPhone(ERROR_PHONE_NOT_REGISTERED)
        }
    }

    companion object {
        const val ERROR_PHONE_NOT_REGISTERED = "Nomor ini tidak terdaftar di Tokopedia."
    }

}