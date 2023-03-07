package com.tokopedia.tokochat.test.robot.state

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.tokochat_common.R
import org.hamcrest.Matchers.not

object StateResult {

    fun assertGlobalErrorIsDisplayed() {
        Espresso.onView(
            withId(R.id.tokochat_include_global_error)
        ).check(matches(isDisplayed()))
    }

    fun assertGlobalErrorIsNotDisplayed() {
        Espresso.onView(
            withId(R.id.tokochat_include_global_error)
        ).check(matches(not(isDisplayed())))
    }

    fun assertGlobalErrorNoConnectionBottomSheet() {
        Espresso.onView(
            withId(R.id.tokochat_global_error)
        ).check(matches(isDisplayed()))
    }

    fun assertUnavailableChatBottomSheet() {
        Espresso.onView(
            withId(com.tokopedia.tokochat.R.id.tokochat_bs_img_unavailable_general)
        ).check(matches(isDisplayed()))
    }

    fun assertReadOnlyReplyArea() {
        Espresso.onView(
            withId(com.tokopedia.tokochat.R.id.tokochat_expired_info)
        ).check(matches(isDisplayed()))
    }
}
