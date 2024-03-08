package com.tokopedia.topchat.chatroom.view.activity.robot.ordercancellation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.matchers.withRecyclerView

object OrderCancellationResult {
    fun assertOrderCancellationWidgetAt(
        position: Int,
        text: String
    ) {
        generalResult {
            assertViewInRecyclerViewAt(
                position,
                R.id.topchat_chatroom_ll_container_order_cancellation,
                isDisplayed()
            )
            assertViewInRecyclerViewAt(
                position,
                R.id.topchat_chatroom_tv_order_cancellation,
                withText(text)
            )
        }
    }

    fun assertOrderCancellationWidgetGoneAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                R.id.topchat_chatroom_ll_container_order_cancellation,
            )
        ).check(doesNotExist())
    }
}
