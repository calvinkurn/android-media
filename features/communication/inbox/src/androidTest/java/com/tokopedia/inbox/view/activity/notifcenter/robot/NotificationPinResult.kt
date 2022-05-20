package com.tokopedia.inbox.view.activity.notifcenter.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.inbox.common.viewmatcher.matchesBackgroundColor
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.notifcenter.R
import org.hamcrest.CoreMatchers.not

object NotificationPinResult {

    fun assertNotificationPinned(position: Int, isShowCountDown: Boolean = false) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.layout_pin_top)
        ).check(matches(isDisplayed()))

        if (isShowCountDown) {
            onView(
                withRecyclerView(R.id.recycler_view)
                    .atPositionOnView(position, R.id.tv_pin_expired)
            ).check(matches(not(isDisplayed())))
        } else {
            onView(
                withRecyclerView(R.id.recycler_view)
                    .atPositionOnView(position, R.id.tv_pin_expired)
            ).check(matches(isDisplayed()))
        }
    }

    fun assertBackgroundColor(position: Int, color: Int?) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notification_container)
        ).check(matches(matchesBackgroundColor(color)))
    }

    fun assertNotificationUnpinned(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.layout_pin_top)
        ).check(matches(not(isDisplayed())))

        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_pin_expired)
        ).check(matches(not(isDisplayed())))
    }
}