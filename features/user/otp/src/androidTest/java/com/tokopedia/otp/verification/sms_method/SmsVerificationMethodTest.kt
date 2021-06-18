package com.tokopedia.otp.verification.sms_method

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.otp.verification.VerificationTest
import org.junit.Test

class SmsVerificationMethodTest : VerificationTest() {

    @Test
    fun test_choose_sms_method_and_resend_otp() {
        runTest {
            clickVerificationMethod(SMS_METHOD_POSITION, SMS_METHOD_TEXT)
            clickResendOtp()
        }
    }

    @Test
    fun test_choose_sms_method_and_type_otp() {
        runTest {
            clickVerificationMethod(SMS_METHOD_POSITION, SMS_METHOD_TEXT)
            inputVerificationOtp(getVerificationMethodUseCase.response.data.modeList[SMS_METHOD_POSITION].otpDigit.toLong())

            assertThat(
                    getAnalyticsWithQuery(gtmLogDbSource, context, VERIFICATION_SMS_METHOD_SUCCESS_TRACKER_PATH),
                    hasAllSuccess()
            )
        }
    }

    companion object {
        private const val SMS_METHOD_POSITION = 0
        private const val SMS_METHOD_TEXT = "SMS"
        private const val VERIFICATION_SMS_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/sms_method/otp_verification_sms_method_success.json"
    }
}