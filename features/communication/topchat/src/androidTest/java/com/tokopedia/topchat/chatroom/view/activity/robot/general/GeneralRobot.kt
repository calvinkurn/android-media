package com.tokopedia.topchat.chatroom.view.activity.robot.general

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object GeneralRobot {

    fun doActionOnListItemAt(
        position: Int,
        viewId: Int,
        action: ViewAction
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                viewId
            )
        ).perform(action)
    }

    fun doScrollChatToPosition(position: Int) {
        onView(withId(R.id.recycler_view_chatroom)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickPlusIconMenu() {
        onView(withId(R.id.topchat_icon_chat_menu))
            .perform(ViewActions.click())
    }

    fun clickComposeArea() {
        onView(withId(R.id.new_comment))
            .perform(ViewActions.click())
    }

    fun typeMessage(msg: String) {
        onView(withId(R.id.new_comment))
            .perform(ViewActions.typeText(msg))
    }

    fun clickStickerIconMenu() {
        onView(withId(R.id.iv_chat_sticker))
            .perform(ViewActions.click())
    }

    fun clickSendBtn() {
        onView(withId(R.id.send_but))
            .perform(ViewActions.click())
    }
}
