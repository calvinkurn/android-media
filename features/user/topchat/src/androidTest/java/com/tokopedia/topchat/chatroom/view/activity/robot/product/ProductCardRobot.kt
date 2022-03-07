package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductCardRobot {

    fun clickBuyButtonAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_buy)
        ).perform(click())
    }

    fun clickATCButtonAt(position: Int) {
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.tv_atc)
        ).perform(click())
    }

    fun clickWishlistButtonAt(position: Int) {
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.tv_wishlist)
        ).perform(click())
    }

}