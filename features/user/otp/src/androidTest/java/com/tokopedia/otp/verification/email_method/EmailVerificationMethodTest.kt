package com.tokopedia.otp.verification.email_method

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.otp.verification.base.VerificationTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class EmailVerificationMethodTest : VerificationTest() {

    @Test
    fun test_choose_email_method_and_type_otp_with_success_result() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        //TODO: problem to check toaster after resend otp { viewToaster(SENT_SMS_TEXT) }
        inputVerificationOtp(getVerificationMethodUseCase.response.data.modeList[EMAIL_METHOD_POSITION].otpDigit.toLong())

        // Then
        // checkTracker(VERIFICATION_EMAIL_METHOD_SUCCESS_TRACKER_PATH)
        //TODO: problem to check tracker
    }

    @Test
    fun test_choose_email_method_and_success_send_otp_result() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        //TODO: problem to check toaster after resend otp { viewToaster(SENT_SMS_TEXT) }

        // Then
        // checkTracker(SEND_OTP_METHOD_TRACKER_PATH)
        //TODO: problem to check tracker
    }

    @Test
    fun test_choose_email_method_with_failed_send_otp_result() {
        // Given
        launchDefaultFragment()

        // When
        setupSendOtpVerificationMethodResponse(false)
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        viewToaster(FAILED_SENT_EMAIL_TEXT)

        // Then
        checkTracker(FAILED_SEND_OTP_METHOD_TRACKER_PATH)
    }

    @Test
    fun test_choose_email_method_and_resend_otp() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        //TODO: problem to check toaster after resend otp { viewToaster(SENT_SMS_TEXT) }
        clickResendOtp()
        //TODO: problem to check toaster after resend otp { viewToaster(SENT_SMS_TEXT) }

        // Then
        checkTracker(CLICK_RESEND_OTP_METHOD_TRACKER_PATH)
    }

    @Test
    fun test_choose_email_method_with_failed_resend_otp_result() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        //TODO: problem to check toaster after resend otp { viewToaster(SENT_SMS_TEXT) }
        setupSendOtpVerificationMethodResponse(false)
        clickResendOtp()
        //TODO: problem to check toaster after resend otp { viewToaster(FAILED_SENT_SMS_TEXT) }

        // Then
        // checkTracker(FAILED_CLICK_RESEND_OTP_METHOD_TRACKER_PATH)
        //TODO: problem to check tracker failed resend otp
    }

    @Test
    fun test_choose_email_method_and_click_choose_other_method() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        //TODO: problem to check toaster after resend otp { viewToaster(SENT_SMS_TEXT) }
        clickChooseAnotherOtpMethod()
        viewLisfOfMethod()

        // Then
        // checkTracker(CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH)
        //TODO: problem to check tracker choose another method
    }

    @Test
    fun test_back_from_verification_page() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(EMAIL_METHOD_POSITION, EMAIL_METHOD_TEXT)
        clickOnBackPress()
        clickOnBackPress()

        // Then
        checkTracker(CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH)
    }

    companion object {
        private const val EMAIL_METHOD_POSITION = 2
        private const val EMAIL_METHOD_TEXT = "E-mail"
        private const val SENT_EMAIL_TEXT = "Sended"
        private const val FAILED_SENT_EMAIL_TEXT = "Failed"
        private const val VERIFICATION_EMAIL_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/email_method/otp_verification_email_method_success.json"
        private const val SEND_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/email_method/send_otp_method.json"
        private const val FAILED_SEND_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/email_method/failed_send_otp_method.json"
        private const val CLICK_RESEND_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/email_method/click_resend_otp_method.json"
        private const val FAILED_CLICK_RESEND_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/email_method/click_resend_otp_method.json"
        private const val CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/email_method/click_choose_another_otp_method.json"
        private const val CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH = "tracker/user/otp/verification/email_method/click_back_from_verification_page.json"
    }
}