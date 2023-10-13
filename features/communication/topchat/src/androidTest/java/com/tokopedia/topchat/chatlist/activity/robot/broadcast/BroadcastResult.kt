package com.tokopedia.topchat.chatlist.activity.robot.broadcast

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withRecyclerView

object BroadcastResult {

    fun assertMVCVoucherVisible(isVisible: Boolean) {
        val matcher = if (isVisible) {
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        } else {
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        }
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.chatlist_img_label_icon)
        )
            .check(matches(matcher))
    }

    fun assertBroadcastFABLayout(isVisible: Boolean) {
        val matcher = if (isVisible) {
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        } else {
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        }
        onView(withIndex(withId(R.id.layout_fab_broadcast), 0))
            .check(matches(matcher))
    }

    fun assertBroadcastFAB(isVisible: Boolean) {
        val matcher = if (isVisible) {
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        } else {
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        }
        onView(withIndex(withId(R.id.iv_fab_broadcast), 0))
            .check(matches(matcher))
        onView(withIndex(withId(R.id.background_fab_broadcast), 0))
            .check(matches(matcher))
        onView(withIndex(withId(R.id.background_shadow_fab_broadcast), 0))
            .check(matches(matcher))
    }

    fun assertBroadcastFABLabel(isVisible: Boolean) {
        val matcher = if (isVisible) {
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        } else {
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        }
        onView(withIndex(withId(R.id.label_new_fab_broadcast), 0))
            .check(matches(matcher))
        onView(withIndex(withId(R.id.background_shadow_label_broadcast), 0))
            .check(matches(matcher))
    }
}
