package com.tokopedia.cart.robot

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.cart.R
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.Assert

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    infix fun buy(func: ResultRobot.() -> Unit): ResultRobot {
        onView(withId(R.id.go_to_courier_page_button)).perform(click())
        return ResultRobot().apply(func)
    }

    fun assertMainContent() {
        onView(withId(R.id.ll_cart_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_cart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertTickerAnnouncement(position: Int) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<TickerAnnouncementViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "assert ticker announcement"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                onView(withId(R.id.cartTicker)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            }
        }))

    }

    fun assertTickerError() {

    }

}

class ResultRobot {

    fun assertGoToShipment() {

    }

}