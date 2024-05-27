package com.tokopedia.topchat.chatroom.view.activity.robot.broadcast

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.matchers.withRecyclerView

object BroadcastRobot {

    fun clickCtaBroadcast(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.ll_cta_container, click())
        }
    }

    fun clickFollowShop(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.btn_follow_shop, click())
        }
    }

    /**
     * New Broadcast
     */
    fun clickBroadcastBanner(position: Int) {
        generalRobot {
            scrollChatToPosition(position + 1) // big size image
            Thread.sleep(100)
            doActionOnListItemAt(position, R.id.topchat_chatroom_broadcast_iv_banner, click())
        }
    }

    fun clickBroadcastPromoProduct(position: Int) {
        scrollBroadcastPromoProduct(position)
        Thread.sleep(100)
        onView(
            withRecyclerView(R.id.topchat_chatroom_broadcast_promo_rv).atPositionOnView(
                position,
                R.id.topchat_chatroom_broadcast_promo_product_card
            )
        ).perform(click())
    }

    fun clickBroadcastPromoSeeMore(position: Int) {
        scrollBroadcastPromoProduct(position)
        Thread.sleep(100)
        onView(
            withRecyclerView(R.id.topchat_chatroom_broadcast_promo_rv).atPositionOnView(
                position,
                R.id.topchat_chatroom_broadcast_see_more_card
            )
        ).perform(click())
    }

    fun scrollBroadcastPromoProduct(position: Int) {
        onView(withId(R.id.topchat_chatroom_broadcast_promo_rv)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickBroadcastFlashSaleProduct(position: Int) {
        scrollBroadcastFlashSaleProduct(position)
        Thread.sleep(100)
        onView(
            withRecyclerView(R.id.topchat_chatroom_broadcast_flashsale_rv).atPositionOnView(
                position,
                R.id.topchat_chatroom_broadcast_flashsale_product_card
            )
        ).perform(click())
    }

    fun clickBroadcastFlashSaleSeeMore(position: Int) {
        scrollBroadcastFlashSaleProduct(position)
        Thread.sleep(100)
        onView(
            withRecyclerView(R.id.topchat_chatroom_broadcast_flashsale_rv).atPositionOnView(
                position,
                R.id.topchat_chatroom_broadcast_see_more_card
            )
        ).perform(click())
    }

    fun scrollBroadcastFlashSaleProduct(position: Int) {
        onView(withId(R.id.topchat_chatroom_broadcast_flashsale_rv)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }
}
