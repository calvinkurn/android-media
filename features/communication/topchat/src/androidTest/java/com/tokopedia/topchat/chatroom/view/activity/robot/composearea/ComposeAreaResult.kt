package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.DrawableMatcher
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object ComposeAreaResult {

    private val errorComposeViewId = R.id.tp_error_compose
    private val sendButtonId = R.id.send_but

    fun assertTypeMessageText(text: String) {
        onView(withId(R.id.new_comment)).check(
            matches(withText(text))
        )
    }

    fun assertSendBtnEnabled() {
        DrawableMatcher.compareDrawable(
            sendButtonId,
            R.drawable.bg_topchat_send_btn
        )
    }

    fun assertSendBtnDisabled() {
        DrawableMatcher.compareDrawable(
            sendButtonId,
            R.drawable.bg_topchat_send_btn_disabled
        )
    }

    fun assertTooLongErrorMsg(msg: String) {
        onView(withText(msg)).check(matches(isDisplayed()))
    }

    fun assertNoTooLongErrorMsg() {
        onView(withId(errorComposeViewId))
            .check(matches(not(isDisplayed())))
    }

    fun assertChatAttachmentMenuVisibility(visibilityMatcher: Matcher<View>) {
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
            matches(visibilityMatcher)
        )
    }

    fun assertAttachmentMenuCount(count: Int) {
        onView(withId(R.id.rv_topchat_attachment_menu)).check(
            matches(withTotalItem(count))
        )
    }

    fun assertChatMenuVisibility(visibilityMatcher: Matcher<View>) {
        onView(withId(R.id.fl_chat_menu)).check(
            matches(visibilityMatcher)
        )
    }

    fun assertTemplateChatVisibility(
        visibilityMatcher: Matcher<View>
    ) {
        onView(withId(R.id.list_template)).check(
            matches(visibilityMatcher)
        )
    }

    fun assertChatStickerMenuVisibility(
        visibilityMatcher: Matcher<View>
    ) {
        onView(withId(R.id.ll_sticker_container)).check(
            matches(visibilityMatcher)
        )
    }
}
