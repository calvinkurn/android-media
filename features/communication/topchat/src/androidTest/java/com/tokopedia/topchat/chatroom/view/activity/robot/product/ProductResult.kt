package com.tokopedia.topchat.chatroom.view.activity.robot.product

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object ProductResult {

    fun hasVisibleRemindMeBtnAt(position: Int) {
        assertRemindMeButtonAt(position, isDisplayed())
    }

    fun hasNoVisibleRemindMeBtnAt(position: Int) {
        assertRemindMeButtonAt(position, not(isDisplayed()))
    }

    fun hasVisibleLabelAtWithText(position: Int, @StringRes stringRes: Int) {
        assertLabelAt(position, isDisplayed())
        assertLabelAt(position, withText(stringRes))
    }

    fun hasNoVisibleEmptyStockLabelAt(position: Int) {
        assertLabelAt(position, not(isDisplayed()))
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

    private fun assertRemindMeButtonAt(position: Int, matcher: Matcher<View>) {
        assertViewInRecyclerViewAt(
            position = position,
            viewId = R.id.tv_wishlist,
            matcher = matcher
        )
    }

    private fun assertLabelAt(position: Int, matcher: Matcher<View>) {
        assertViewInRecyclerViewAt(
            position = position,
            viewId = R.id.lb_product_label,
            matcher = matcher
        )
    }

    private fun assertAtcButtonAt(position: Int, matcher: Matcher<View>) {
        assertViewInRecyclerViewAt(
            position = position,
            viewId = R.id.tv_atc,
            matcher = matcher
        )
    }

    private fun assertBuyButtonAt(position: Int, matcher: Matcher<View>) {
        assertViewInRecyclerViewAt(
            position = position,
            viewId = R.id.tv_buy,
            matcher = matcher
        )
    }
}