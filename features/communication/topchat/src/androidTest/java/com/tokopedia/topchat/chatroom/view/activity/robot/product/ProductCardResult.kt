package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.assertion.withItemCount
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.equalTo

object ProductCardResult {

    fun hasFailedToasterWithMsg(msg: String) {
        onView(withSubstring(msg))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun hasToasterWithMsg(msg: String) {
        onView(withText(msg))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun hasVariantLabel(@IdRes variantResourceId: Int, variantText: String, position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, variantResourceId)
        )
            .check(matches(withText(variantText)))
    }

    fun hasProductAttachmentAtPosition(position: Int) {
        onView(
            withId(R.id.recycler_view_chatroom)
        ).check(
            atPositionIsInstanceOf(
                position,
                ProductAttachmentUiModel::class.java
            )
        )
    }

    fun hasProductPreviewAttachmentAtPosition(position: Int) {
        onView(
            withId(R.id.recycler_view_chatroom)
        ).check(
            atPositionIsInstanceOf(
                position,
                TopchatProductAttachmentPreviewUiModel::class.java
            )
        )
    }

    fun hasProductBuyButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
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

    fun hasProductWishlistButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_wishlist)
        )
            .check(matches(withText(buttonText)))
    }

    fun hasProductStockButtonWithText(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.btn_update_stock)
        )
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun hasProductCarouselWithTotal(position: Int, total: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.rv_product)
        ).check(
            withItemCount(equalTo(total))
        )
    }

    fun hasProductCarouselBuyButtonWithText(buttonText: String, position: Int) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.tv_buy)
        )
            .check(matches(withText(buttonText)))
    }

    fun hasProductCarouselStockButtonWithText(position: Int) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.btn_update_stock)
        )
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
