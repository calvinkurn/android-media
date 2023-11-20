package com.tokopedia.topchat.chatroom.view.activity.robot.product

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.assertion.withItemCount
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.equalTo
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
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.tv_buy)
        )
            .check(matches(withText(buttonText)))
    }

    fun hasProductCarouselBroadcastBuyButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.rv_product_carousel)
                .atPositionOnView(position, R.id.tv_buy)
        )
            .check(matches(withText(buttonText)))
    }

    fun assertProductAttachmentAtPosition(position: Int) {
        onView(
            withId(R.id.recycler_view_chatroom)
        ).check(
            atPositionIsInstanceOf(
                position,
                ProductAttachmentUiModel::class.java
            )
        )
    }

    fun assertProductPreviewAttachmentAtPosition(position: Int) {
        onView(
            withId(R.id.recycler_view_chatroom)
        ).check(
            atPositionIsInstanceOf(
                position,
                TopchatProductAttachmentPreviewUiModel::class.java
            )
        )
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

    fun assertStockCountBtnOnCarouselAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.btn_update_stock)
        ).check(matches(matcher))
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

    fun assertProductCarouselWithTotal(position: Int, total: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.rv_product)
        ).check(
            withItemCount(equalTo(total))
        )
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
