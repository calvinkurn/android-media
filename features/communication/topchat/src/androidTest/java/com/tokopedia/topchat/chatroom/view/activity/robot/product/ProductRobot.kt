package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductRobot {

    fun clickBuyButtonAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_buy)
        ).perform(click())
    }

    fun clickATCButtonAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_atc)
        ).perform(click())
    }

    fun clickWishlistButtonAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.tv_wishlist)
        ).perform(click())
    }

    fun clickProductAttachmentAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.cl_info)
        ).perform(click())
    }

    fun clickChangeStockBtnAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.btn_update_stock, click())
        }
    }

    fun clickChangeStockBtnOnCarouselAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.btn_update_stock)
        ).perform(click())
    }

    fun clickChangeStockBtnOnCarouselBroadcastAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_product_carousel)
                .atPositionOnView(position, R.id.btn_update_stock)
        ).perform(click())
    }
}
