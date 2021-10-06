package com.tokopedia.topchat.chatroom.view.activity.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.longClick
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ReplyBubbleRobot {

    fun longClickBubbleAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, R.id.fxChat
            )
        ).perform(longClick())
    }

}