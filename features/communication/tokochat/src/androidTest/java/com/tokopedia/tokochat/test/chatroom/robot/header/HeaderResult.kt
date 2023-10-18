package com.tokopedia.tokochat.test.chatroom.robot.header

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not

object HeaderResult {

    fun assertHeaderDisplayed(interlocutorName: String, licensePlate: String) {
        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_toolbar)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_text_user_title)
        ).check(matches(withText(interlocutorName)))

        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_text_user_subtitle)
        ).check(matches(withText(licensePlate)))
    }

    fun assertCallButtonHeader(isDisabled: Boolean) {
        val matcher = if (isDisabled) {
            not(isClickable())
        } else {
            isClickable()
        }
        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_icon_header_menu)
        ).check(matches(matcher))
    }

    fun assertOrderHistory(merchantName: String, timeDelivery: String) {
        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_transaction_order)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_tp_order_name)
        ).check(matches(withText(merchantName)))

        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_tp_estimate_value)
        ).check(matches(withText(timeDelivery)))
    }

    fun assertOrderHistoryLocalLoad() {
        onView(
            withId(com.tokopedia.tokochat_common.R.id.tokochat_localload_error_transaction_widget)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}
