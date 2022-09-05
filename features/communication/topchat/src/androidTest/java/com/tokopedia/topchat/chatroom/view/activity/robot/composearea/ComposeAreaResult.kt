package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.DrawableMatcher
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not

object ComposeAreaResult {

    private val errorComposeViewId = R.id.tp_error_compose
    private val sendButtonId = R.id.send_but

    fun assertSendBtnEnabled() {
        DrawableMatcher.compareDrawableWithIndex(
            sendButtonId, R.drawable.bg_topchat_send_btn, 0)
    }

    fun assertSendBtnDisabled() {
        DrawableMatcher.compareDrawableWithIndex(
            sendButtonId, R.drawable.bg_topchat_send_btn_disabled, 0)
    }

    fun assertTooLongErrorMsg(msg: String) {
        onView(withText(msg)).check(matches(isDisplayed()))
    }

    fun assertNoTooLongErrorMsg() {
        onView(withIndex(withId(errorComposeViewId), 0))
            .check(matches(not(isDisplayed())))
    }

    fun assertAttachmentMenuCount(count: Int) {
        onView(withIndex(withId(R.id.rv_topchat_attachment_menu), 0)).check(
            matches(withTotalItem(count))
        )
    }

}