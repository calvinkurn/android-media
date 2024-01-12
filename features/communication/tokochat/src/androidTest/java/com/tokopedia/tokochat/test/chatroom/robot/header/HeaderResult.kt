package com.tokopedia.tokochat.test.chatroom.robot.header

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import com.tokopedia.tokochat_common.R as tokochat_commonR

object HeaderResult {

    fun assertHeaderDisplayed(interlocutorName: String, licensePlate: String) {
        onView(
            withId(tokochat_commonR.id.tokochat_toolbar)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(
            withId(tokochat_commonR.id.tokochat_text_user_title)
        ).check(matches(withText(interlocutorName)))

        onView(
            withId(tokochat_commonR.id.tokochat_text_user_subtitle)
        ).check(matches(withText(licensePlate)))
    }

    fun assertCallButtonHeader(isDisabled: Boolean) {
        val matcher = if (isDisabled) {
            not(isClickable())
        } else {
            isClickable()
        }
        onView(
            withId(tokochat_commonR.id.tokochat_icon_header_menu)
        ).check(matches(matcher))
    }

    fun assertOrderHistoryTokoFood(merchantName: String, timeDelivery: String) {
        onView(
            withId(tokochat_commonR.id.tokochat_transaction_order)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(
            withId(tokochat_commonR.id.tokochat_tp_order_name)
        ).check(matches(withText(merchantName)))

        onView(
            withId(tokochat_commonR.id.tokochat_tp_estimate_value)
        ).check(matches(withText(timeDelivery)))
    }

    fun assertOrderHistoryLocalLoad() {
        onView(
            withId(tokochat_commonR.id.tokochat_localload_error_transaction_widget)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun assertOrderHistoryLogistic(
        productName: String,
        additionalInfo: String,
        estimateLabel: String,
        estimateValue: String
    ) {
        onView(
            withId(tokochat_commonR.id.tokochat_transaction_order)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(
            withId(tokochat_commonR.id.tokochat_logistic_tp_order_name)
        ).check(matches(withText(productName)))

        if (additionalInfo.isBlank()) {
            onView(
                withId(tokochat_commonR.id.tokochat_logistic_tp_additional_info)
            ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        } else {
            onView(
                withId(tokochat_commonR.id.tokochat_logistic_tp_additional_info)
            ).check(matches(withText(additionalInfo)))
        }

        onView(
            withId(tokochat_commonR.id.tokochat_logistic_tp_estimate_label)
        ).check(matches(withText(estimateLabel)))

        onView(
            withId(tokochat_commonR.id.tokochat_logistic_tp_estimate_value)
        ).check(matches(withText(estimateValue)))

        onView(
            withId(tokochat_commonR.id.tokochat_logistic_btn_order_progress)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}
