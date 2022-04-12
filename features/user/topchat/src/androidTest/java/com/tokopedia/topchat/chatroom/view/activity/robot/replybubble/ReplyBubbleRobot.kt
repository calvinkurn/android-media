package com.tokopedia.topchat.chatroom.view.activity.robot.replybubble

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuRobot.clickLongClickMenuItemAt
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleMatcher.matchReplyBoxChildWithId
import com.tokopedia.topchat.matchers.withRecyclerView

object ReplyBubbleRobot {

    const val REPLY_ITEM_MENU_POSITION = 0

    fun longClickBubbleAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, R.id.fxChat
            )
        ).perform(longClick())
    }

    fun clickReplyItemMenu(position: Int? = null) {
        clickLongClickMenuItemAt(position ?: REPLY_ITEM_MENU_POSITION)
    }

    fun clickReplyComposeCloseIcon() {
        onView(
            matchReplyBoxChildWithId(R.id.iv_rb_close)
        ).perform(click())
    }

    fun clickReplyCompose() {
        onView(
            matchReplyBoxChildWithId(R.id.trb_container)
        ).perform(click())
    }

    fun clickReplyBubbleAt(position: Int) {
        doActionOnReplyBubbleAt(position, R.id.cl_reply_container, click())
    }

    private fun doActionOnReplyBubbleAt(
        position: Int,
        viewId: Int,
        action: ViewAction
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, viewId
            )
        ).perform(action)
    }

}