package com.tokopedia.topchat.chatroom.view.activity.robot.shippinglocation

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt

object ShippingLocationResult {
    fun hasVisibleShippingLocationOn(position: Int) {
        assertViewInRecyclerViewAt(
            position = position,
            viewId = R.id.tv_shipping_location,
            matcher = isDisplayed()
        )
    }

    fun hasShippingLocationOnWithText(position: Int, text: String) {
        assertViewInRecyclerViewAt(
            position = position,
            viewId = R.id.tv_shipping_location,
            matcher = withText(text)
        )
    }
}