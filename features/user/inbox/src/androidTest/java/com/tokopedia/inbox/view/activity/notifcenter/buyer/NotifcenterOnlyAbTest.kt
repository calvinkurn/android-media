package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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

    // TODO: should use specific page total notification counter if `showBottomNav` is false
    // TODO: what happened to the onboarding if `showBottomNav` is false

}