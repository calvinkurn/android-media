package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.inbox.R
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
        onView(withId(R.id.bottom_nav_top_shadow)).check(matches(not(isDisplayed())))
        onView(withId(R.id.inbox_bottom_nav)).check(matches(not(isDisplayed())))
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
        onView(withId(R.id.unread_header_counter)).check(
            matches(
                withText(expectedTotalSellerNotifCounter.toString())
            )
        )
    }

    // TODO: what happened to the onboarding if `showBottomNav` is false

}