package com.tokopedia.privacycenter.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.privacycenter.R

class PrivacyCenterRobot {
    fun scrollToBottom() {
        onView(withText("Butuh info lebih lanjut?"))
            .perform(nestedScrollTo())
    }

    fun clickRiwayatKebijakan() {
        onView(withId(R.id.menuListPrivacyPolicy)).perform(click())
    }
}

class PrivacyResultRobot {
    fun shouldShowCorrectName(name: String) {
        onView(withId(R.id.text_name)).check(matches(withText(name)))
    }
    fun shouldShowIconLinked() {
        onView(withId(R.id.ic_account_linked)).check(matches(isDisplayed()))
    }
    fun shouldShowRecommendationSection() {
        onView(withText("Rekomendasi & promo")).check(matches(isDisplayed()))
    }
    fun shouldDisplayPrivacyTestData() {
        onView(withText("Privacy test - 06 Apr 2021"))
            .check(matches(isDisplayed()))
    }
}

fun privacyCenterRobot(func: PrivacyCenterRobot.() -> Unit): PrivacyCenterRobot {
    return PrivacyCenterRobot().apply(func)
}

infix fun PrivacyCenterRobot.assert(func: PrivacyResultRobot.() -> Unit): PrivacyResultRobot {
    return PrivacyResultRobot().apply(func)
}
