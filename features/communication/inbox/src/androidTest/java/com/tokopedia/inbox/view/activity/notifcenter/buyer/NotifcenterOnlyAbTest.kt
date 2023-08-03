package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.inbox.view.activity.base.InboxAssertion
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class NotifcenterOnlyAbTest : InboxNotifcenterTest() {

    @Test
    fun should_have_no_bottom_nav_if_showBottomNav_is_false() {
        // Given
        startInboxActivity {
            setShowBottomNav(it, false)
        }

        // Then
        InboxAssertion.assertTotalGlobalNavIcon(2) // Cart & Nav Global Icon
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

    @Test
    fun should_show_title_as_Notifikasi_when_showBottomNav_is_false_and_user_has_no_shop() {
        // Given
        val expectedTitle = "Notifikasi"
        inboxDep.apply {
            userSession.fakeHasShop = false
        }
        startInboxActivity {
            setShowBottomNav(it, false)
        }

        // Then
        InboxAssertion.assertInboxTitle(expectedTitle)
    }

    @Test
    fun should_show_title_as_Inbox_when_showBottomNav_is_true_and_user_has_no_shop() {
        // Given
        val expectedTitle = "Inbox"
        inboxDep.apply {
            userSession.fakeHasShop = false
        }
        startInboxActivity {
            setShowBottomNav(it, true)
        }

        // Then
        InboxAssertion.assertInboxTitle(expectedTitle)
    }
}
