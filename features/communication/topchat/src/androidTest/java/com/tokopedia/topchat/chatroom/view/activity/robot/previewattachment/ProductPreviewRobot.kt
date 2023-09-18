package com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductPreviewRobot {

    fun clickRetryButtonAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_attachment_preview)
                .atPositionOnView(position, R.id.iu_retry_product_preview)
        ).perform(click())
    }

    fun clickCloseAttachmentPreview(position: Int) {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                position,
                ClickChildViewWithIdAction()
                    .clickChildViewWithId(R.id.iv_close)
            )
        onView(ViewMatchers.withId(R.id.rv_attachment_preview)).perform(viewAction)
    }
}
