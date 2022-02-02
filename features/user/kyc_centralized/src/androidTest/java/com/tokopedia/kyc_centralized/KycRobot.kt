package com.tokopedia.kyc_centralized

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.kyc_centralized.test.R

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
        Thread.sleep(1_000)
        onView(withId(R.id.image_button_shutter)).perform(click())
    }

    fun atCameraClickNext() {
        Thread.sleep(1_000)
        onView(withId(R.id.next_button)).perform(click())
    }

    fun atFaceIntroClickNext() {
        Thread.sleep(1_000)
        onView(withId(R.id.button)).perform(click())
    }

    fun atFinalPressCta() {
        Thread.sleep(3_000)
        onView(withId(R.id.upload_button)).perform(click())
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
    Thread.sleep(1500)
    return KycResultRobot().apply(func)
}