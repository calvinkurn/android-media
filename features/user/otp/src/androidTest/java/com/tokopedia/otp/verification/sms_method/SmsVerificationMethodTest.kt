package com.tokopedia.otp.verification.sms_method

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.VerificationTest
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import org.hamcrest.core.AllOf
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SmsVerificationMethodTest : VerificationTest() {

    @Test
    fun test_show_sms_in_list_otp_method() {
        // GIVEN
        setupVerificationActivity()
        setupSmsVerificationMethodResponse(true)
        inflateTestFragment()

        // THEN
        clickVerificationMethod()
        inputVerificationOtp()

        assert(true)

        ViewMatchers.assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, context, VERIFICATION_SMS_METHOD_SUCCESS_TRACKER_PATH),
                hasAllSuccess()
        )
    }

    private fun clickVerificationMethod() {
        val viewInteraction = Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.method_list))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItem<VerificationMethodAdapter.ViewHolder>(ViewMatchers.hasDescendant(ViewMatchers.withText(SMS_METHOD)), ViewActions.click()))
    }

    private fun inputVerificationOtp() {
        Espresso.onView(ViewMatchers.withId(R.id.pin))
                .perform(ViewActions.typeText("1234"))
    }

    companion object {
        private const val SMS_METHOD = "SMS"

        private const val VERIFICATION_SMS_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/sms_method/otp_verification_sms_method_success.json"
    }
}