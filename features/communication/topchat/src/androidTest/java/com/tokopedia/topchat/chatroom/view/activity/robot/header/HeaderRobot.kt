package com.tokopedia.topchat.chatroom.view.activity.robot.header

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R

object HeaderRobot {

    fun clickThreeDotsMenu() {
        onView(withId(com.tokopedia.chat_common.R.id.header_menu)).perform(click())
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
