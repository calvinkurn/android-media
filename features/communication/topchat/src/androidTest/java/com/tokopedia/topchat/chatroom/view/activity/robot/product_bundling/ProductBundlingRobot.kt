package com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductBundlingRobot {

    fun clickCtaProductBundling(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.button_open_package)
        ).perform(click())
    }

    fun doScrollProductBundlingToPosition(position: Int) {
        onView(CommonMatcher.firstView(withId(R.id.rv_product_bundle_card))).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickOnImageMultipleItemBundling() {
        onView(CommonMatcher.firstView(withId(R.id.iv_product_bundling_thumbnail)))
            .perform(click())
    }

    fun clickOnImageSingleItemBundling() {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(0, R.id.iv_single_product_thumbnail)
        ).perform(click())
    }

    fun doScrollProductBundlingToPositionInBroadcast(position: Int) {
        onView(CommonMatcher.firstView(withId(R.id.rv_product_bundle_card_broadcast))).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }
}