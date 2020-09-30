package com.tokopedia.cart.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.cart.R

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    infix fun buy(func: ResultRobot.() -> Unit): ResultRobot {
        onView(withId(R.id.go_to_courier_page_button)).perform(click())
        return ResultRobot().apply(func)
    }

    fun assertTickerAnnouncement() {

    }

    fun assertTickerError() {

    }

}

class ResultRobot {

    fun assertGoToShipment() {

    }

}