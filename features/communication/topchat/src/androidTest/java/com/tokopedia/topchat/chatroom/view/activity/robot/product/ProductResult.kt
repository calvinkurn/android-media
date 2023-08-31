package com.tokopedia.topchat.chatroom.view.activity.robot.product

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object ProductResult {

    fun hasVariantLabel(@IdRes variantResourceId: Int, variantText: String, position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, variantResourceId)
        )
            .check(matches(withText(variantText)))
    }

    fun hasProductBuyButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_buy)
        )
            .check(matches(withText(buttonText)))
    }

    fun hasProductCarouselBuyButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.rv_product_carousel)
                .atPositionOnView(position, R.id.tv_buy)
        )
            .check(matches(withText(buttonText)))
    }

    fun hasProductWishlistButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_wishlist)
        )
            .check(matches(withText(buttonText)))
    }

    fun hasVisibleRemindMeBtnAt(position: Int) {
        assertRemindMeButtonAt(position, isDisplayed())
    }

    fun hasNoVisibleRemindMeBtnAt(position: Int) {
        assertRemindMeButtonAt(position, not(isDisplayed()))
    }

    fun hasVisibleLabelAtWithText(position: Int, @StringRes stringRes: Int) {
        this.assertLabelAt(position, isDisplayed())
        this.assertLabelAt(position, withText(stringRes))
    }

    fun hasNoVisibleEmptyStockLabelAt(position: Int) {
        this.assertLabelAt(position, not(isDisplayed()))
    }

    fun hasVisibleAtcBtnAt(position: Int) {
        assertAtcButtonAt(position, isDisplayed())
    }

    fun hasNoVisibleAtcBtnAt(position: Int) {
        assertAtcButtonAt(position, not(isDisplayed()))
    }

    fun hasVisibleBuyBtnAt(position: Int) {
        assertBuyButtonAt(position, isDisplayed())
    }

    fun hasNoVisibleBuyBtnAt(position: Int) {
        assertBuyButtonAt(position, not(isDisplayed()))
    }

    fun hasProductName(position: Int, string: String) {
        assertProductNameAt(position, withText(string))
    }

    fun hasProductPrice(position: Int, string: String) {
        assertProductPrice(position, withText(string))
    }

    private fun assertRemindMeButtonAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tv_wishlist,
                matcher = matcher
            )
        }
    }

    fun assertLabelAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.lb_product_label,
                matcher = matcher
            )
        }
    }

    fun assertLabelOnCarouselAt(position: Int, matcher: Matcher<View>) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.lb_product_label)
        ).check(matches(matcher))
    }

    fun assertLabelOnCarouselBroadcastAt(position: Int, matcher: Matcher<View>) {
        onView(
            withRecyclerView(R.id.rv_product_carousel)
                .atPositionOnView(position, R.id.lb_product_label)
        ).check(matches(matcher))
    }

    private fun assertAtcButtonAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tv_atc,
                matcher = matcher
            )
        }
    }

    private fun assertBuyButtonAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tv_buy,
                matcher = matcher
            )
        }
    }

    private fun assertProductNameAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tv_product_name,
                matcher = matcher
            )
        }
    }

    private fun assertProductPrice(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tv_price,
                matcher = matcher
            )
        }
    }

    fun assertContainerProductAttachment(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.containerProductAttachment,
                matcher = matcher
            )
        }
    }

    fun assertFooterProductAttachment(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.ll_footer,
                matcher = matcher
            )
        }
    }

    fun assertProductStockTypeAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tp_seller_stock_category,
                matcher = matcher
            )
        }
    }

    fun assertStockCountAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tp_seller_stock_count,
                matcher = matcher
            )
        }
    }

    fun assertStockCountBtnAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.btn_update_stock,
                matcher = matcher
            )
        }
    }

    fun assertStockCountOnCarouselAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.tp_seller_stock_count)
        ).check(matches(matcher))
    }

    fun assertStockCountOnCarouselBroadcastAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.rv_product_carousel)
                .atPositionOnView(position, R.id.tp_seller_stock_count)
        ).check(matches(matcher))
    }

    fun assertTokoCabangAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.ll_seller_fullfilment,
                matcher = matcher
            )
        }
    }
}
