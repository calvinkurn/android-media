package com.tokopedia.pdpCheckout.testing.cart.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import com.tokopedia.cart.R as cartR

fun cartPage(func: CartPageRobot.() -> Unit) = CartPageRobot().apply(func)

class CartPageRobot {

    fun waitForData() {
        Thread.sleep(2000)
    }

    fun clickBuyButton() {
        onView(withId(cartR.id.go_to_courier_page_button)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isDisplayed()
            }

            override fun getDescription(): String {
                return "click buy button"
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.performClick()
            }
        })
    }
}
