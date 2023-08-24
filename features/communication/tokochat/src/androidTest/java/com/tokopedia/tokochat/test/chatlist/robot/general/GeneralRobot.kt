package com.tokopedia.tokochat.test.chatlist.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.tokochat.stub.common.matcher.smoothScrollTo
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R as tokochat_commonR

object GeneralRobot {
    fun clickChatListItem(position: Int) {
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_list_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_list_layout_item)
        ).perform(ViewActions.click())
    }

    fun scrollToPosition(position: Int) {
        onView(withId(tokochat_commonR.id.tokochat_list_rv)).perform(
            smoothScrollTo(position)
        )
    }
}
