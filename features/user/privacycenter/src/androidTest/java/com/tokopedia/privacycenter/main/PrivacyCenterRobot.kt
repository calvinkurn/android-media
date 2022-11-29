package com.tokopedia.privacycenter.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.privacycenter.R

class PrivacyCenterRobot {

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
}

fun privacyCenterRobot(func: PrivacyCenterRobot.() -> Unit): PrivacyCenterRobot {
    return PrivacyCenterRobot().apply(func)
}

infix fun PrivacyCenterRobot.assert(func: PrivacyResultRobot.() -> Unit): PrivacyResultRobot {
    return PrivacyResultRobot().apply(func)
}
