package com.tokopedia.topchat.chatroom.view.activity.robot.imageattachment

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ImageAttachmentRobot {

    fun longClickImageAt(position: Int) {
        Espresso.onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, R.id.image
            )
        ).perform(ViewActions.longClick())
    }

}