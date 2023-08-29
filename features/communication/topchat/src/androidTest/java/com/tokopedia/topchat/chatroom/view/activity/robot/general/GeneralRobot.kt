package com.tokopedia.topchat.chatroom.view.activity.robot.general

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.RecyclerViewAction
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

    fun scrollChatToPosition(position: Int) {
        onView(withId(R.id.recycler_view_chatroom)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun smoothScrollChatToPosition(position: Int) {
        onView(withId(R.id.recycler_view_chatroom)).perform(
            RecyclerViewAction.smoothScrollTo(position)
        )
    }
}
