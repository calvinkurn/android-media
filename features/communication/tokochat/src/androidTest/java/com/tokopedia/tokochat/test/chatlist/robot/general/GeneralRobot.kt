package com.tokopedia.tokochat.test.chatlist.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R

object GeneralRobot {
    fun clickChatListItem(position: Int) {
        onView(
            withRecyclerView(R.id.tokochat_list_rv)
                .atPositionOnView(position, R.id.tokochat_list_layout_item)
        ).perform(ViewActions.click())
    }
}
