package com.tokopedia.otp.verification.sms_method

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.matcher.TestUtils.withRecyclerView
import com.tokopedia.otp.verification.VerificationTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.streams.asSequence

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SmsVerificationMethodTest : VerificationTest() {

    @Test
    fun test_show_sms_in_list_otp_method() {
        runTest {
            clickVerificationMethod(0)
            inputVerificationOtp(6)

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, VERIFICATION_SMS_METHOD_SUCCESS_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    private fun clickVerificationMethod(position: Int) {
        onView(withRecyclerView(com.tokopedia.otp.R.id.method_list).atPositionOnView(position, com.tokopedia.otp.R.id.container)).perform(click())
    }

    private fun inputVerificationOtp(length: Long) {
        val otp = generateRandomOtp(length)
        Thread.sleep(1000)
        onView(allOf(withId(com.tokopedia.pin.R.id.pin_text_field), isDescendantOfA(withId(com.tokopedia.otp.R.id.pin))))
                .check(matches(isDisplayed()))
    }

    private fun generateRandomOtp(length: Long): String {
        val source = "0123456789"
        return java.util.Random().ints(length, 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
    }

    companion object {
        private const val SMS_METHOD = "SMS"

        private const val VERIFICATION_SMS_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/sms_method/otp_verification_sms_method_success.json"
    }
}