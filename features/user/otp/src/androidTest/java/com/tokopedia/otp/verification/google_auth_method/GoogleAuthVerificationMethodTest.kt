package com.tokopedia.otp.verification.google_auth_method

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.otp.verification.base.VerificationTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class GoogleAuthVerificationMethodTest : VerificationTest() {

    @Test
    fun test_choose_google_auth_method_and_type_otp_with_success_result() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
        inputVerificationOtp(getVerificationMethodUseCase.response.data.modeList[GOOGLE_AUTH_METHOD_POSITION].otpDigit.toLong())

        // Then
        checkTracker(VERIFICATION_GOOGLE_AUTH_METHOD_SUCCESS_TRACKER_PATH)
    }

    @Test
    fun test_choose_google_auth_method_and_click_choose_other_method() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
        clickChooseAnotherOtpMethod()
        viewLisfOfMethod()

        // Then
        checkTracker(CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH)
    }

    @Test
    fun test_back_from_verification_page() {
        // Given
        launchDefaultFragment()

        // When
        clickVerificationMethod(GOOGLE_AUTH_METHOD_POSITION, GOOGLE_AUTH_METHOD_TEXT)
        clickOnBackPress()
        clickOnBackPress()

        // Then
        checkTracker(CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH)
    }

    companion object {
        private const val GOOGLE_AUTH_METHOD_POSITION = 3
        private const val GOOGLE_AUTH_METHOD_TEXT = "Google Authenticator"
        private const val VERIFICATION_GOOGLE_AUTH_METHOD_SUCCESS_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/otp_verification_google_auth_method_success.json"
        private const val CLICK_CHOOSE_ANOTHER_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/click_choose_another_otp_method.json"
        private const val CLICK_BACK_FROM_VERIFICATION_PAGE_TRACKER_PATH = "tracker/user/otp/verification/google_auth_method/click_back_from_verification_page.json"
    }
}