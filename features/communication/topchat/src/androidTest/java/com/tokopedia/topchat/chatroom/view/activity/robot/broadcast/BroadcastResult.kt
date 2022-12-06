package com.tokopedia.topchat.chatroom.view.activity.robot.broadcast

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import org.hamcrest.CoreMatchers.not

object BroadcastResult {

    fun assertBroadcastShown() {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(0, BroadCastUiModel::class.java)
        )
    }

    fun assertBroadcastSpamHandler() {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(0, BroadcastSpamHandlerUiModel::class.java)
        )
    }

    fun assertBroadcastCtaText(text: String, match: Boolean = true) {
        val matcher = if (match) {
            withText(text)
        } else {
            not(withText(text))
        }
        onView(withId(R.id.topchat_cta_broadcast_tv)).check(
            matches(matcher)
        )
    }
}
