package com.tokopedia.inbox.universalinbox.test.robot.menu

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.inbox.universalinbox.stub.common.atPositionCheckInstanceOf
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSectionUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel

object MenuResult {

    fun assertMenuSectionOnPosition(position: Int, reverse: Boolean = false) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = UniversalInboxMenuSectionUiModel::class.java,
                reverse = reverse
            )
        )
    }

    fun assertNotificationCounter(counterText: String) {
        onView(withId(R.id.notification))
            .check(matches(withText(counterText)))
    }

    fun assertNotificationCounterGone() {
        onView(withId(R.id.notification))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertMenuCounter(position: Int, counterText: String) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_notification_icon)
        ).check(matches(withText(counterText)))
    }

    fun assertMenuCounterGone(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_notification_icon)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertShopInfo(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_layout_shop_info)
        ).check(matches(isDisplayed()))
    }

    fun assertSellerChatMenu(position: Int, hasShop: Boolean) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = UniversalInboxMenuUiModel::class.java,
                reverse = !hasShop
            )
        )
    }
}
