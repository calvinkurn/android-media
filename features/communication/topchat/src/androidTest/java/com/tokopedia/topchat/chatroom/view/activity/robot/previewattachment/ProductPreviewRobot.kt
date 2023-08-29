package com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductPreviewRobot {

    fun clickRetryButtonAt(position: Int) {
        onView(
                withRecyclerView(R.id.rv_attachment_preview)
                        .atPositionOnView(position, R.id.iu_retry_product_preview)
        ).perform(click())
    }

}
