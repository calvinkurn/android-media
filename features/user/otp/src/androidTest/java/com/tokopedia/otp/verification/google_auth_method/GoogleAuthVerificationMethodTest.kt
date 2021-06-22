package com.tokopedia.otp.verification.google_auth_method

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
class GoogleAuthVerificationMethodTest : VerificationTest() {

    @Test
    fun test_choose_google_auth_method_and_resend_otp() {
        runTest {
            clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
            clickResendOtp()
        }
    }

    @Test
    fun test_choose_google_auth_method_and_click_choose_other_method() {
        runTest {
            clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
            clickChooseAnotherOtpMethod()
            viewLisfOfMethod()

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    @Test
    fun test_choose_google_auth_method_and_type_otp_with_success_result() {
        runTest {
            clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
            inputVerificationOtp(getVerificationMethodUseCase.response.data.modeList[GOOGLE_AUTH_METHOD_POSITION].otpDigit.toLong())

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, VERIFICATION_GOOGLE_AUTH_METHOD_SUCCESS_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    @Test
    fun test_choose_google_auth_method_with_failed_send_otp_result() {
        runTest {
            setupSendOtpVerificationMethodResponse(false)
            clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
        }
    }

    @Test
    fun test_back_from_verification_page() {
        runTest {
            clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
            clickOnBackPress()

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    companion object {
        private const val GOOGLE_AUTH_METHOD_POSITION = 3
        private const val GOOGLE_AUTH_METHOD_TEXT = "Google Authenticator"
        private const val VERIFICATION_GOOGLE_AUTH_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/otp_verification_google_auth_method_success.json"
        private const val CLICK_RESEND_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/click_resend_otp_method.json"
        private const val CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/click_choose_another_otp_method.json"
        private const val CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/click_back_from_verification_page.json"
    }
}