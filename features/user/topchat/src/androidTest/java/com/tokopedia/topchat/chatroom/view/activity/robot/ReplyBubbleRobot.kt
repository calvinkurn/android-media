package com.tokopedia.topchat.chatroom.view.activity.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.longClick
import com.tokopedia.topchat.R
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

    fun clickLongClickMenuItemAt(position: Int) {
        onView(
            withRecyclerView(R.id.rvMenu).atPositionOnView(
                position, R.id.ll_long_click_menu_item
            )
        ).perform(ViewActions.click())
    }

}