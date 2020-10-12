package com.tokopedia.cart.robot

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.cart.R
import com.tokopedia.cart.view.viewholder.CartItemViewHolder
import com.tokopedia.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.Assert

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    infix fun buy(func: ResultRobot.() -> Unit): ResultRobot {
        Log.d("CartHappyFlowTest", "Do buy")
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

    fun assertFirstCartShopViewHolder(position: Int,
                                      shopName: String,
                                      shopLocation: String,
                                      productName: String,
                                      productVariant: String) {
        Log.d("CartHappyFlowTest", "Start Test First shop item")
        onView(withId(R.id.rv_cart)).perform(RecyclerViewActions.actionOnItemAtPosition<CartShopViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "performing assertion action on first CartShopViewHolder"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                Log.d("CartHappyFlowTest", "Start Assert First shop item")
                Assert.assertEquals(shopName, view.findViewById<Typography>(R.id.tv_shop_name).text)
                Assert.assertEquals(shopLocation, view.findViewById<Typography>(R.id.tv_fulfill_district).text)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageView>(R.id.img_shop_badge).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.label_fulfillment).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageUnify>(R.id.img_free_shipping).visibility)
                Assert.assertEquals(View.VISIBLE, view.findViewById<RecyclerView>(R.id.rv_cart_item).visibility)
                Assert.assertTrue(view.findViewById<CheckBox>(R.id.cb_select_shop).isChecked)

                assertFirstShopFirstCartItemViewHolder(
                        view = view,
                        recyclerViewId = R.id.rv_cart_item,
                        position = position,
                        productName = productName,
                        productVariant = productVariant
                )
            }
        }))
    }

    private fun assertFirstShopFirstCartItemViewHolder(view: View,
                                                       recyclerViewId: Int,
                                                       position: Int,
                                                       productName: String,
                                                       productVariant: String) {
        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)
        val childItemCount = childRecyclerView.adapter?.itemCount ?: 0
        Log.d("CartHappyFlowTest", "Inner Item Count " + childItemCount)

        for (i in 0 until childItemCount) {
            try {
                Log.d("CartHappyFlowTest", "Start Test Inner RV")
                onView((withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
/*
                        .perform(RecyclerViewActions.actionOnItemAtPosition<CartItemViewHolder>(position, object : ViewAction {
                            override fun getDescription(): String = "performing assertion action on first CartItemViewHolder"

                            override fun getConstraints(): Matcher<View>? = null

                            override fun perform(uiController: UiController?, view: View) {
                                Log.d("CartHappyFlowTest", "Assert Inner RV")
                                Assert.assertEquals(productName, view.findViewById<Typography>(R.id.text_product_name).text)
                                Assert.assertEquals(productVariant, view.findViewById<Typography>(R.id.text_product_variant).text)
                                Assert.assertEquals(View.VISIBLE, view.findViewById<ImageView>(R.id.iv_image_product))
                                Assert.assertTrue(view.findViewById<CheckBox>(R.id.cb_select_item).isChecked)
                            }
                        }))
*/
            } catch (e: PerformException) {
                Log.d("CartHappyFlowTest", "Got Exception")
                e.printStackTrace()
            }
        }

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