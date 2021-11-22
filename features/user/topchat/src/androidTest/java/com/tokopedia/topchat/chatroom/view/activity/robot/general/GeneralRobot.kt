package com.tokopedia.topchat.chatroom.view.activity.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
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
                position, viewId
            )
        ).perform(action)
    }
}