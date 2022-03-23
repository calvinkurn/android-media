package com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

object ProductBundlingResult {

    fun assertMultiBundlingShown() {
        assertProductBundleCard(R.id.single_bundling_layout, matches(not(isDisplayed())))
        assertProductBundleCard(R.id.rv_product_bundle, matches(isDisplayed()))
    }

    fun assertSingleBundlingShown() {
        assertProductBundleCard(R.id.single_bundling_layout, matches(isDisplayed()))
        assertProductBundleCard(R.id.rv_product_bundle, matches(not(isDisplayed())))
    }

    fun assertCtaBundlingShown() {
        assertProductBundleCard(R.id.button_open_package, matches(isDisplayed()))
        assertProductBundleCard(R.id.button_open_package, matches(withText("Lihat Paket")))
        assertProductBundleCard(R.id.button_open_package, matches(isEnabled()))
    }

    fun assertCtaBundlingNotShown() {
        assertProductBundleCard(R.id.button_open_package, matches(not(isDisplayed())))
    }

    fun labelSingleBundlingShown(text: String) {
        assertProductBundleCard(R.id.label_package, matches(withText(text)))
    }

    fun assertCarouselBundlingShown(expectedCount: Int) {
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(0, R.id.rv_product_bundle_card))
            .check(matches(withTotalItem(expectedCount)))
    }

    fun assertCtaOutOfStock() {
        assertProductBundleCard(R.id.button_open_package, matches(isDisplayed()))
        assertProductBundleCard(R.id.button_open_package, matches(withText("Paket Habis")))
        assertProductBundleCard(R.id.button_open_package, matches(not(isEnabled())))
    }

    private fun assertProductBundleCard(viewId: Int, matcher: ViewAssertion) {
        onView(
            allOf(
                isDescendantOfA(withRecyclerView(R.id.recycler_view_chatroom).atPosition(0)),
                isDescendantOfA(withRecyclerView(R.id.rv_product_bundle_card).atPosition(0)),
                withId(viewId)
            )
        ).check(matcher)
    }
}