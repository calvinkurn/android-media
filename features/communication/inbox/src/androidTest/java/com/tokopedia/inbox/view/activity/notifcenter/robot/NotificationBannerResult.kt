package com.tokopedia.inbox.view.activity.notifcenter.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.notifcenter.R

object NotificationBannerResult {

    fun assertNotificationTypeBanner(position: Int) {
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.txt_notification_type))
            .check(matches(withText("Promo")))
    }

    fun assertNotificationBannerTitle(position: Int, title: String) {
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.txt_notification_title))
            .check(matches(withText(title)))
    }

    fun assertNotificationBannerCountDown(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        } else {
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
        }
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.tv_status))
            .check(matcher)
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.tu_countdown))
            .check(matcher)
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.tv_end_date))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertNotificationBannerExpiredDate(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        } else {
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
        }
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.tv_status))
            .check(matcher)
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.tu_countdown))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(position, R.id.tv_end_date))
            .check(matcher)
    }
}