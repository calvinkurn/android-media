package com.tokopedia.notifcenter.test.robot.filter

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.withRecyclerView
import com.tokopedia.notifcenter.test.robot.general.GeneralResult.assertRecyclerviewItem
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotificationOrderListViewHolder
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object FilterResult {
    fun assertNotificationOrderList(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                NotificationOrderListViewHolder::class.java
            )
        )
    }

    fun assertNotNotificationOrderList() {
        assertRecyclerviewItem(
            not(
                hasViewHolderOf(
                    NotificationOrderListViewHolder::class.java
                )
            )
        )
    }

    fun assertNotifOrderCardTextAtPosition(
        position: Int,
        msg: String
    ) {
        onView(
            withRecyclerView(R.id.rv_order_list)
                .atPositionOnView(position, R.id.tp_order_title)
        ).check(ViewAssertions.matches(ViewMatchers.withText(msg)))
    }

    fun assertOrderWidgetCardAt(
        position: Int,
        matcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.rv_order_list)
                .atPositionOnView(position, R.id.ll_card_uoh)
        ).check(ViewAssertions.matches(matcher))
    }
}
