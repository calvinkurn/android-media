package com.tokopedia.kyc_centralized

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.kyc_centralized.test.R
import kotlinx.android.synthetic.main.layout_kyc_upload_error.*

class KycRobot {

    fun checkTermsAndCondition() {
        onView(withId(R.id.kyc_benefit_checkbox)).perform(click())
    }

    fun atInfoClickNext() {
        onView(withId(R.id.kyc_benefit_btn)).perform(click())
    }

    fun atKtpIntroClickNext() {
        onView(withId(R.id.button)).perform(click())
    }

    fun atCameraClickCapture() {
        Thread.sleep(500)
        onView(withId(R.id.image_button_shutter)).perform(click())
    }

    fun atCameraClickNext() {
        Thread.sleep(1000)
        onView(withId(R.id.next_button)).perform(click())
    }

    fun atFaceIntroClickNext() {
        Thread.sleep(1_000)
        onView(withId(R.id.button)).perform(click())
    }

    fun atFinalPressCta() {
        Thread.sleep(2_500)
        onView(withId(R.id.upload_button)).perform(click())
    }

    fun atFinalPressErrorButton() {
        Thread.sleep(2000)
        onView(withId(R.id.kyc_upload_error_button)).perform(click())
    }

}

class KycResultRobot {

    fun shouldShowPendingPage() {
        onView(withText(R.string.kyc_pending_title)).check(matches(isDisplayed()))
    }

}

fun kycRobot(func: KycRobot.() -> Unit): KycRobot {
    return KycRobot().apply(func)
}

infix fun KycRobot.upload(func: KycResultRobot.() -> Unit): KycResultRobot {
    // in KYC, there is no manual CTA Button to upload, so we just wait
    Thread.sleep(2500)
    return KycResultRobot().apply(func)
}