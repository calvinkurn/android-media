package com.tokopedia.cart.robot

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.cart.R
import com.tokopedia.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.holder_item_cart_ticker_error.view.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
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

    fun assertTickerAnnouncementViewHolder(position: Int) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<TickerAnnouncementViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on TickerAnnouncementViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(View.VISIBLE, view.findViewById<Ticker>(R.id.cartTicker).visibility)
            }
        }))
    }

    fun assertCartTickerErrorViewHolder(position: Int, message: String) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartTickerErrorViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on CartTickerErrorViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(message, view.findViewById<Typography>(R.id.ticker_description).text)
            }
        }))
    }

    fun assertFirstCartShopViewHolder(position: Int, shopName: String, shopLocation: String) {
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartShopViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Assert.assertEquals(shopName, view.findViewById<Typography>(R.id.tv_shop_name).text)
                Assert.assertEquals(shopLocation, view.findViewById<Typography>(R.id.tv_fulfill_district).text)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageView>(R.id.img_shop_badge))
                Assert.assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.label_fulfillment))
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageUnify>(R.id.img_free_shipping))
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageUnify>(R.id.rv_cart_item))
                Assert.assertTrue(view.findViewById<CheckBox>(R.id.cb_select_shop).isChecked)
            }
        }))
    }

    fun assertSecondCartShopViewHolder() {

    }

    fun assertThirdCartShopViewHolder() {

    }

}

class ResultRobot {

    fun assertGoToShipment() {

    }

}