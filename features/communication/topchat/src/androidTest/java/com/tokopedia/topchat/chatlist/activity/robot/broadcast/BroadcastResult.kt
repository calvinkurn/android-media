package com.tokopedia.topchat.chatlist.activity.robot.broadcast

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object BroadcastResult {

    fun assertMVCVoucherVisible(isVisible: Boolean) {
        val matcher = if (isVisible) {
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        } else {
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        }
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(0, R.id.chatlist_img_label_icon))
            .check(matches(matcher))
    }
}
