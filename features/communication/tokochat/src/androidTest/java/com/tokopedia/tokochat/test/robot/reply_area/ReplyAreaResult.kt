package com.tokopedia.tokochat.test.robot.reply_area

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokochat_common.R
import org.hamcrest.Matchers

object ReplyAreaResult {

    fun assertReplyAreaIsDisplayed() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertTypeReplyAreaText(text: String) {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertButtonReply(isDisabled: Boolean) {
        val matcher = if (isDisabled) {
            Matchers.not(ViewMatchers.isClickable())
        } else {
            ViewMatchers.isClickable()
        }
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_ic_send_btn)
        ).check(ViewAssertions.matches(matcher))
    }
}
