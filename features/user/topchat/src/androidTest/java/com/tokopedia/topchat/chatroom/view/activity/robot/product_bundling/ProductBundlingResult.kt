package com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling

import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not

object ProductBundlingResult {

    fun assertMultiBundlingShown() {
        assertViewInRecyclerViewAt(0, R.id.single_bundling_layout, not(isDisplayed()))
        assertViewInRecyclerViewAt(0, R.id.rv_product_bundle, isDisplayed())
    }

    fun assertSingleBundlingShown() {
        assertViewInRecyclerViewAt(0, R.id.rv_product_bundle, not(isDisplayed()))
        assertViewInRecyclerViewAt(0, R.id.single_bundling_layout, isDisplayed())
    }

    fun assertCtaBundlingShown() {
        assertViewInRecyclerViewAt(0, R.id.button_open_package, isDisplayed())
        assertViewInRecyclerViewAt(0, R.id.button_open_package, withText("Lihat Paket"))
        assertViewInRecyclerViewAt(0, R.id.button_open_package, isEnabled())
    }

    fun assertCtaBundlingNotShown() {
        assertViewInRecyclerViewAt(0, R.id.button_open_package, not(isDisplayed()))
    }

    fun labelSingleBundlingShown(text: String) {
        assertViewInRecyclerViewAt(0, R.id.label_package, withText(text))
    }

    fun assertCarouselBundlingShown(expectedCount: Int) {
        assertViewInRecyclerViewAt(0, R.id.rv_product_bundle_card, withTotalItem(expectedCount))
    }

    fun assertCtaOutOfStock() {
        assertViewInRecyclerViewAt(0, R.id.button_open_package, isDisplayed())
        assertViewInRecyclerViewAt(0, R.id.button_open_package, withText("Paket Habis"))
        assertViewInRecyclerViewAt(0, R.id.button_open_package, not(isEnabled()))
    }
}