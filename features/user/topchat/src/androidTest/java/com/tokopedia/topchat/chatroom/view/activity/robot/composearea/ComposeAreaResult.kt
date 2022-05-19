package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.DrawableMatcher
import org.hamcrest.CoreMatchers.not

object ComposeAreaResult {

    private val errorComposeViewId = R.id.tp_error_compose
    private val sendButtonId = R.id.send_but

    fun assertSendBtnEnabled() {
        DrawableMatcher.compareDrawable(sendButtonId, R.drawable.bg_topchat_send_btn)
    }

    fun assertSendBtnDisabled() {
        DrawableMatcher.compareDrawable(sendButtonId, R.drawable.bg_topchat_send_btn_disabled)
    }

    fun assertTooLongErrorMsg(msg: String) {
        onView(withId(errorComposeViewId))
            .check(matches(isDisplayed()))
            .check(matches(withText(msg)))
    }

    fun assertNoTooLongErrorMsg() {
        onView(withId(errorComposeViewId))
            .check(matches(not(isDisplayed())))
    }


}