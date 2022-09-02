package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductPreviewRobot {

    fun clickRetryButtonAt(position: Int) {
        Espresso.onView(
                withRecyclerView(R.id.rv_attachment_preview)
                        .atPositionOnView(position, R.id.iu_retry_product_preview)
        ).perform(click())
    }

}