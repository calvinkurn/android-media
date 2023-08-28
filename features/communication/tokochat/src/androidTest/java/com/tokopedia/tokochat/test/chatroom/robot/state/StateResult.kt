package com.tokopedia.tokochat.test.chatroom.robot.state

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.tokochat_common.R
import org.hamcrest.Matchers.not

object StateResult {

    fun assertGlobalErrorIsDisplayed() {
        onView(
            withId(R.id.tokochat_include_global_error)
        ).check(matches(isDisplayed()))
    }

    fun assertGlobalErrorIsNotDisplayed() {
        onView(
            withId(R.id.tokochat_include_global_error)
        ).check(matches(not(isDisplayed())))
    }

    fun assertGlobalErrorNoConnectionBottomSheet() {
        onView(
            withId(R.id.tokochat_global_error)
        ).check(matches(isDisplayed()))
    }

    fun assertUnavailableChatBottomSheet() {
        onView(
            withId(com.tokopedia.tokochat.R.id.tokochat_bs_img_unavailable_general)
        ).check(matches(isDisplayed()))
    }

    fun assertReadOnlyReplyArea() {
        onView(withId(R.id.tokochat_expired_info)).check(matches(isDisplayed()))
    }
}
