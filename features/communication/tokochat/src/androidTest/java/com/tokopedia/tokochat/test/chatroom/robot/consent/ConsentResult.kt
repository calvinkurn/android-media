package com.tokopedia.tokochat.test.chatroom.robot.consent

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokochat_common.R

object ConsentResult {

    fun assertConsentChatBottomSheet(isDisplayed: Boolean) {
        val matcher = if (isDisplayed) {
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        } else {
            doesNotExist()
        }
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_image_consent)
        ).check(matcher)
    }
}
