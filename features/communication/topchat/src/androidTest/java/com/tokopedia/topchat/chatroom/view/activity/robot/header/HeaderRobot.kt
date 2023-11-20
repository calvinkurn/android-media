package com.tokopedia.topchat.chatroom.view.activity.robot.header

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.chat_common.R as chat_commonR

object HeaderRobot {

    fun clickThreeDotsMenu() {
        onView(withId(chat_commonR.id.header_menu)).perform(click())
    }

    fun clickFollowMenu() {
        clickMenuAt(R.string.follow_store)
    }

    fun clickFollowingMenu() {
        clickMenuAt(R.string.already_follow_store)
    }

    fun clickReportUserMenu() {
        clickMenuAt(R.string.chat_report_user)
    }

    fun clickBlockUser() {
        clickMenuAt(R.string.title_block_user_chat)
    }

    fun clickUnBlockUser() {
        clickMenuAt(R.string.title_unblock_user_chat)
    }

    fun clickChatSettingMenu() {
        clickMenuAt(R.string.title_chat_setting)
    }

    fun clickDeleteChat() {
        clickMenuAt(R.string.delete_conversation)
    }

    fun clickConfirmDeleteChat() {
        clickMenuAt(R.string.topchat_chat_delete_confirm)
    }

    private fun clickMenuAt(@StringRes title: Int) {
        onView(withText(title)).perform(click())
    }
}
