package com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling

import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductBundlingResult {

    fun assertMultiBundlingShown(position: Int) {
        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.rv_product_bundle)
            .matches(isDisplayed())

        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.tv_product_bundling_name)
            .matches(doesNotExist())
    }

    fun assertSingleBundlingShown(position: Int) {
        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.tv_product_bundling_name)
            .matches(isDisplayed())

        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.rv_product_bundle)
            .matches(doesNotExist())
    }

    fun assertCtaBundlingShown(position: Int) {
        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.button_open_package)
            .matches(isDisplayed())
    }

    fun assertCtaBundlingNotShown(position: Int) {
        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.button_open_package)
            .matches(doesNotExist())
    }

    fun labelSingleBundlingShown(position: Int, text: String) {
        withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.label_package)
            .matches(withText(text))
    }

    fun assertCarouselBundlingShown(position: Int) {

    }
}