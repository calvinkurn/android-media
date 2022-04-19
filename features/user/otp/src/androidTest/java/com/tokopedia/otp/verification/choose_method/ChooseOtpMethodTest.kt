package com.tokopedia.otp.verification.choose_method

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.otp.verification.base.VerificationTest
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ChooseOtpMethodTest : VerificationTest() {

    @Test
    fun test_view_otp_method() {
        // Given
        launchDefaultFragment()

        // When
        viewLisfOfMethod()

        // Then
        checkTracker(VIEW_CHOOSE_OTP_METHOD_TRACKER_PATH)
    }

    @Test
    fun test_click_inactive_phone() {
        // Given
        launchDefaultFragment()

        // When
        clickInactivePhone()

        // Then
        checkTracker(CLICK_INACTIVE_PHONE_TRACKER_PATH)
    }

    @Test
    fun test_click_back() {
        // Given
        launchDefaultFragment()

        // When
        clickOnBackPress()

        // Then
        checkTracker(CLICK_BACK_TRACKER_PATH)
    }

    companion object {
        const val VIEW_CHOOSE_OTP_METHOD_TRACKER_PATH = "tracker/user/otp/verification/choose_method/view_choose_method.json"
        const val CLICK_INACTIVE_PHONE_TRACKER_PATH = "tracker/user/otp/verification/choose_method/click_inactive_phone_in_choose_method.json"
        const val CLICK_BACK_TRACKER_PATH = "tracker/user/otp/verification/choose_method/click_back_in_choose_method.json"
    }
}