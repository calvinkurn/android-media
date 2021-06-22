package com.tokopedia.otp.verification.wa_method

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.verification.base.VerificationTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class WaVerificationMethodTest : VerificationTest() {

    @Test
    fun test_choose_wa_method_and_resend_otp() {
        runTest {
            clickVerificationMethod(WA_METHOD_POSITION, WA_METHOD_TEXT)
            clickResendOtp()
        }
    }

    @Test
    fun test_choose_wa_method_and_click_choose_other_method() {
        runTest {
            clickVerificationMethod(WA_METHOD_POSITION, WA_METHOD_TEXT)
            clickChooseAnotherOtpMethod()
            viewLisfOfMethod()

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    @Test
    fun test_choose_wa_method_and_type_otp_with_success_result() {
        runTest {
            clickVerificationMethod(WA_METHOD_POSITION, WA_METHOD_TEXT)
            inputVerificationOtp(getVerificationMethodUseCase.response.data.modeList[WA_METHOD_POSITION].otpDigit.toLong())

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, VERIFICATION_WA_METHOD_SUCCESS_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    @Test
    fun test_choose_wa_method_with_failed_send_otp_result() {
        runTest {
            setupSendOtpVerificationMethodResponse(false)
            clickVerificationMethod(WA_METHOD_POSITION, WA_METHOD_TEXT)
        }
    }

    @Test
    fun test_choose_wa_method_with_failed_resend_otp_result() {
        runTest {
            clickVerificationMethod(WA_METHOD_POSITION, WA_METHOD_TEXT)
            setupSendOtpVerificationMethodResponse(false)
            clickResendOtp()
        }
    }

    @Test
    fun test_back_from_verification_page() {
        runTest {
            clickVerificationMethod(WA_METHOD_POSITION, WA_METHOD_TEXT)
            clickOnBackPress()

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    companion object {
        private const val WA_METHOD_POSITION = 1
        private const val WA_METHOD_TEXT = "WhatsApp"
        private const val VERIFICATION_WA_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/wa_method/otp_verification_wa_method_success.json"
        private const val CLICK_RESEND_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/wa_method/click_resend_otp_method.json"
        private const val CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/wa_method/click_choose_another_otp_method.json"
        private const val CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH = "tracker/user/otp/verification/wa_method/click_back_from_verification_page.json"
    }
}