package com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R

object PreviewAttachmentRobot {

    fun scrollToPosition(position: Int) {
        onView(withId(R.id.rv_attachment_preview)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2)
        )
    }
}
