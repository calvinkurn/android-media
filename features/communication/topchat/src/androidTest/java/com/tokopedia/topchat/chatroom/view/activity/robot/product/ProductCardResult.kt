package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductCardResult {

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
}
