package com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.allOf

object LongClickBubbleMenuRobot {

    fun clickLongClickMenuItemAt(position: Int) {
        onView(
            withRecyclerView(R.id.rvMenu).atPositionOnView(
                position, R.id.ll_long_click_menu_item
            )
        ).perform(click())
    }

    fun clickDeleteMsgMenu() {
        onView(
            allOf(
                isDescendantOfA(withId(R.id.rvMenu)),
                withText(R.string.title_topchat_delete_msg)
            )
        ).perform(click())
    }

    fun clickConfirmDeleteMsgDialog() {
        onView(
            allOf(
                withId(com.tokopedia.dialog.R.id.dialog_btn_primary),
                withText(R.string.topchat_action_delete_msg_bubble_confirmation)
            )
        ).perform(click())
    }

}