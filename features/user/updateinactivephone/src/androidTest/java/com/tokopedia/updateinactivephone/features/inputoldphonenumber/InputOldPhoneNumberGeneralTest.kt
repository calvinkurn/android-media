package com.tokopedia.updateinactivephone.features.inputoldphonenumber

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.checkErrorMessageIsNotDisplayed
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.checkErrorMessageOnInputPhone
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.checkInitViewIsShowing
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction.setPhoneNumberText
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class InputOldPhoneNumberGeneralTest : BaseInputOldPhoneNumberTest() {

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            inputOldPhoneNumberUseCaseStub.response = registerCheckRegisteredModel
        }
    }

    @Test
    fun init_view_is_show() {
        //WHEN first time launch

        //THEN
        runTest {
            checkInitViewIsShowing()
        }
    }

    @Test
    fun input_empty_phone_number_then_show_message_error() {
        //GIVEN
        val phone = ""

        runTest {
            //WHEN
            setPhoneNumberText(phone)
            clickOnButtonSubmit()

            //THEN
            checkErrorMessageOnInputPhone(ERROR_PHONE_EMPTY)
        }
    }

    @Test
    fun input_too_long_phone_number_then_show_message_error() {
        //GIVEN
        val phone = "0821375674837463231"

        runTest {
            //WHEN
            setPhoneNumberText(phone)
            clickOnButtonSubmit()

            //THEN
            checkErrorMessageOnInputPhone(ERROR_PHONE_TOO_LONG)
        }
    }

    @Test
    fun input_too_long_short_number_then_show_message_error() {
        //GIVEN
        val phone = "0821375"

        runTest {
            //WHEN
            setPhoneNumberText(phone)
            clickOnButtonSubmit()

            //THEN
            checkErrorMessageOnInputPhone(ERROR_PHONE_TOO_SHORT)
        }
    }

    @Test
    fun input_registered_number_then_success() {
        //GIVEN
        val phone = "082137567654"

        runTest {
            //WHEN
            setPhoneNumberText(phone)
            clickOnButtonSubmit()

            //THEN
            checkErrorMessageIsNotDisplayed()
        }
    }

    companion object {
        const val ERROR_PHONE_EMPTY = "Wajib diisi."
        const val ERROR_PHONE_TOO_SHORT = "Min. 9 digit."
        const val ERROR_PHONE_TOO_LONG = "Maks. 15 digit."
    }

}