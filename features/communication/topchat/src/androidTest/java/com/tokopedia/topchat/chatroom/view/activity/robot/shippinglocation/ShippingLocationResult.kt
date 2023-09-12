package com.tokopedia.topchat.chatroom.view.activity.robot.shippinglocation

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object ShippingLocationResult {
    fun hasVisibleShippingLocationOn(position: Int) {
        assertShippingLocationAt(position, isDisplayed())
    }

    fun hasNoVisibleShippingLocationOn(position: Int) {
        assertShippingLocationAt(position, not(isDisplayed()))
    }

    fun hasShippingLocationOnWithText(position: Int, text: String) {
        assertShippingLocationAt(position, withText(text))
    }

    private fun assertShippingLocationAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(
                position = position,
                viewId = R.id.tv_shipping_location,
                matcher = matcher
            )
        }
    }
}
