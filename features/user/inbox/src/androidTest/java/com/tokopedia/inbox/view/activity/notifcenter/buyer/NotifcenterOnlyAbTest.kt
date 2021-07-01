package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.inbox.view.activity.base.InboxAssertion
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class NotifcenterOnlyAbTest : InboxNotifcenterTest() {

    @Test
    fun should_have_no_bottom_nav_if_showBottomNav_is_false() {
        // Given
        startInboxActivity {
            setShowBottomNav(it, false)
        }

        // Then
        InboxAssertion.assertTotalGlobalNavIcon(1)
        InboxAssertion.assertBottomNavShadowVisibility(not(isDisplayed()))
        InboxAssertion.assertBottomNavVisibility(not(isDisplayed()))
    }

    @Test
    fun should_use_specific_page_total_notification_counter_if_showBottomNav_is_false() {
        // Given
        val expectedTotalSellerNotifCounter = 10
        inboxDep.apply {
            inboxNotificationUseCase.response = defaultNotifCounter
        }
        startInboxActivity {
            setShowBottomNav(it, false)
        }

        // Then
        InboxAssertion.assertTotalSwitchRoleCounter(expectedTotalSellerNotifCounter.toString())
    }

}