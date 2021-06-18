package com.tokopedia.otp.verification.choose_method

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.verification.VerificationTest
import com.tokopedia.otp.verification.sms_method.SmsVerificationMethodTest
import org.junit.Test

class ChooseOtpMethodTest : VerificationTest() {

    @Test
    fun test_view_otp_method() {
        runTest {
            viewLisfOfMethod()

            ViewMatchers.assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, VIEW_CHOOSE_OTP_METHOD_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    @Test
    fun test_click_inactive_phone() {
        runTest {
            clickInactivePhone()

            ViewMatchers.assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, CLICK_INACTIVE_PHONE_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    @Test
    fun test_click_back() {
        runTest {
            clickOnBackPress()

            ViewMatchers.assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, CLICK_BACK_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    companion object {
        const val VIEW_CHOOSE_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/choose_method/view_choose_method.json"
        const val CLICK_INACTIVE_PHONE_TRACKER_PATH = "tracker/user/otp/verification/choose_method/click_inactive_phone_in_choose_method.json"
        const val CLICK_BACK_TRACKER_PATH = "tracker/user/otp/verification/choose_method/click_back_in_choose_method.json"
    }
}