package com.tokopedia.updateinactivephone.features.inputoldphonenumber.cassava

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.BaseInputOldPhoneNumberTest
import com.tokopedia.updateinactivephone.features.inputoldphonenumber.InputOldPhoneNumberAction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class InputOldPhoneNumberCassavaNetworkTest : BaseInputOldPhoneNumberTest() {

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    @Test
    fun input_empty_phone_number_then_show_message_error() {
        // Given

        runTest {
            // When
            InputOldPhoneNumberAction.clickOnButtonSubmit()
        }
        activityInputOldPhoneNumberRule.finishActivity()

        // Then
        checkCassavaTest()
    }

    @Test
    fun input_too_long_phone_number_then_show_message_error() {
        // Given
        val phone = "0821375674837463231"

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()
        }
        activityInputOldPhoneNumberRule.finishActivity()

        // Then
        checkCassavaTest()
    }

    @Test
    fun input_too_long_short_number_then_show_message_error() {
        // Given
        val phone = "0821375"

        runTest {
            // When
            InputOldPhoneNumberAction.setPhoneNumberText(phone)
            InputOldPhoneNumberAction.clickOnButtonSubmit()
        }
        activityInputOldPhoneNumberRule.finishActivity()

        // Then
        checkCassavaTest()
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
        }
        activityInputOldPhoneNumberRule.finishActivity()

        // Then
        checkCassavaTest()
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
        }
        activityInputOldPhoneNumberRule.finishActivity()

        // Then
        checkCassavaTest()
    }

    private fun checkCassavaTest() {
        ViewMatchers.assertThat(
            cassavaRule.validate("254"),
            hasAllSuccess()
        )
    }

}