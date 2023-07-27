package com.tokopedia.tokochat.test.chatroom.robot.reply_area

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.tokochat_common.R

object ReplyAreaResult {

    fun assertReplyAreaIsDisplayed() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertTypeReplyAreaText(text: String) {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).check(ViewAssertions.matches(withText(text)))
    }

    fun assertSnackbarText(text: String) {
        Espresso.onView(withText(text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertNoSnackbarText(text: String) {
        Espresso.onView(withText(text)).check(ViewAssertions.doesNotExist())
    }

    fun assertReplyAreaErrorMessage(text: String) {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tv_error_message)
        ).check(ViewAssertions.matches(withText(text)))
    }
}
