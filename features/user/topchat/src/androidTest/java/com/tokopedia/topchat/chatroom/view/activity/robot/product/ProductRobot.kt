package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import org.hamcrest.CoreMatchers.allOf

object ProductRobot {

    fun clickSingleProductCardAt(position: Int) {
        performClickOnProductCard(R.id.recycler_view_chatroom, position)
    }

    fun clickCarouselProductCardAt(position: Int) {
        performClickOnProductCard(R.id.rv_product_carousel, position)
    }

    fun clickSingleProductCardAtcButtonAt(position: Int) {
        performClickAtcButton(R.id.recycler_view_chatroom, position)
    }

    fun clickCarouselProductCardAtcButtonAt(position: Int) {
        performClickAtcButton(R.id.rv_product_carousel, position)
    }

    fun clickSingleProductCardBuyButtonAt(position: Int) {
        performClickBuyButton(R.id.recycler_view_chatroom, position)
    }

    fun clickCarouselProductCardBuyButtonAt(position: Int) {
        performClickBuyButton(R.id.recycler_view_chatroom, position)
    }

    private fun performClickOnProductCard(@IdRes recyclerViewId: Int, position: Int) {
        val viewAction =
            actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                position, click()
            )
        onView(
            allOf(
                isDisplayed(),
                withId(recyclerViewId)
            )
        ).perform(viewAction)
    }

    private fun performClickAtcButton(@IdRes recyclerViewId: Int, position: Int) {
        val viewAction =
            actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                position,
                ClickChildViewWithIdAction()
                    .clickChildViewWithId(R.id.tv_atc)
            )
        onView(
            allOf(
                isDisplayed(),
                withId(recyclerViewId)
            )
        ).perform(viewAction)
    }

    private fun performClickBuyButton(@IdRes recyclerViewId: Int, position: Int) {
        val viewAction =
            actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                position,
                ClickChildViewWithIdAction()
                    .clickChildViewWithId(R.id.tv_buy)
            )
        onView(
            allOf(
                isDisplayed(),
                withId(recyclerViewId)
            )
        ).perform(viewAction)
    }
}