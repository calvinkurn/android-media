package com.tokopedia.updateinactivephone.features.inputoldphonenumber.cassava

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.BaseInputOldPhoneNumberTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.cassava.InputOldPhoneNumberCassava.validateTrackerOnSubmitPhone
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class InputOldPhoneNumberCassavaLocalTest : BaseInputOldPhoneNumberTest() {

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Test
    fun init_view_is_show() {
        // When first time launch

        // Then
        runTest {
            InputOldPhoneNumberAction.checkInitViewIsShowing()
        }
    }

    @Test
    fun input_empty_phone_number_then_show_message_error() {
        // Given
        val phone = ""

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()

            // Then
            cassavaRule.validateTrackerOnSubmitPhone(
                state = InputOldPhoneNumberCassavaState.EMPTY_PHONE
            )
        }
    }

    @Test
    fun input_too_long_phone_number_then_show_message_error() {
        // Given
        val phone = "0821375674837463231"

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()

            // Then
            cassavaRule.validateTrackerOnSubmitPhone(
                state = InputOldPhoneNumberCassavaState.TOO_LONG_PHONE
            )
        }
    }

    @Test
    fun input_too_short_number_then_show_message_error() {
        // Given
        val phone = "0821375"

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()

            // Then
            cassavaRule.validateTrackerOnSubmitPhone(
                state = InputOldPhoneNumberCassavaState.TOO_SHORT_PHONE
            )
        }
    }

    @Test
    fun input_registered_number_then_success() {
        // Given
        inactivePhoneDependency.apply {
            inputOldPhoneNumberUseCaseStub.response = registerCheckRegisteredModel
        }
        val phone = "082137567654"

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()

            // Then
            cassavaRule.validateTrackerOnSubmitPhone(
                state = InputOldPhoneNumberCassavaState.REGISTERED_PHONE
            )
        }
    }

    @Test
    fun input_not_registered_number_then_success() {
        // Given
        inactivePhoneDependency.apply {
            inputOldPhoneNumberUseCaseStub.response = registerCheckNotRegisteredModel
        }
        val phone = "012345678910"

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()

            // Then
            cassavaRule.validateTrackerOnSubmitPhone(
                state = InputOldPhoneNumberCassavaState.UNREGISTERED_PHONE
            )
        }
    }

}