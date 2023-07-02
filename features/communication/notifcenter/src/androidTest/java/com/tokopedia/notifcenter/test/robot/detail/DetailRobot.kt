package com.tokopedia.notifcenter.test.robot.detail

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.withRecyclerView
import com.tokopedia.test.application.matcher.RecyclerViewMatcher

object DetailRobot {

    fun scrollToProductPosition(position: Int) {
        onView(withId(R.id.rv_carousel_product)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickLoadMoreAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.btn_loading)
        ).perform(click())
    }

    fun clickCheckoutButtonAt(position: Int) {
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(position, R.id.btn_checkout)
        ).perform(click())
    }

    fun clickAtcAt(position: Int) {
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(0, R.id.btn_atc)
        ).perform(click())
    }

    fun clickCheckWishlist(position: Int) {
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(position, R.id.tv_check_wishlist)
        ).perform(click())
    }

    fun clickProductAt(position: Int) {
        onView(
            RecyclerViewMatcher(R.id.rv_carousel_product)
                .atPositionOnView(position, R.id.cl_product)
        ).perform(click())
    }
}
