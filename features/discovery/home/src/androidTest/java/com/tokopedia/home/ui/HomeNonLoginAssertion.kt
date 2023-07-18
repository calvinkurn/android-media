package com.tokopedia.home.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.home.R
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.test.application.espresso_component.CommonAssertion
import com.tokopedia.test.application.espresso_component.CommonMatcher

/**
 * Created by frenzel 11/07/23
 */
interface HomeNonLoginAssertion {
    /**
     * We want to make sure that the navigation toolbar:
     * - Showing correct icon for homepage (MESSAGE, CART, and NAVIGATION)
     * - Showing correct notification counter based on home mock response
     * (GLOBAL NAV only showing dot icon)
     */
    fun assertNavigationToolbar(context: Context) {
        Espresso.onView(CommonMatcher.withTagStringValue(IconList.NAME_MESSAGE))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(
            CommonMatcher.withTagStringValue(
                HomeTagHelper.getNotifCounterMessage(
                    context
                )
            )
        ).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        val messageCounter = HomeMockValueHelper.DEFAULT_COUNTER_INBOX_TICKET_VALUE +
            HomeMockValueHelper.DEFAULT_COUNTER_INBOX_REVIEW_VALUE +
            HomeMockValueHelper.DEFAULT_COUNTER_INBOX_TALK_VALUE +
            HomeMockValueHelper.DEFAULT_COUNTER_CHAT_UNREAD_VALUE
        Espresso.onView(
            CommonMatcher.withTagStringValue(
                HomeTagHelper.getNotifCounterMessage(
                    context
                )
            )
        ).check(
            ViewAssertions.matches(
                ViewMatchers.withText(messageCounter.toString())
            )
        )

        Espresso.onView(CommonMatcher.withTagStringValue(IconList.NAME_CART))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(CommonMatcher.withTagStringValue(HomeTagHelper.getNotifCounterCart(context)))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )
        Espresso.onView(CommonMatcher.withTagStringValue(HomeTagHelper.getNotifCounterCart(context)))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(HomeMockValueHelper.DEFAULT_COUNTER_NOTIF_VALUE.toString())
                )
            )

        Espresso.onView(CommonMatcher.withTagStringValue(IconList.NAME_NAV_GLOBAL))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(CommonMatcher.withTagStringValue(HomeTagHelper.getNotifGlobalNav(context))).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )

        Espresso.onView(
            CommonMatcher.withTagStringValue(
                context.getString(R.string.tag_navigation_toolbar_searchbar)
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * We want to make sure that the home header:
     * - Showing header background image
     * - Showing choose address widget
     */
    fun assertHeader() { }

    /**
     * We want to make sure that the home recyclerview:
     * - Showing data equally with given mock response and the static data (header + recom)
     */
    fun assertHomeContent(totalData: Int) {
        /**
         * Assert home content to match given mock value
         */
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemCountAssertion(
                totalData
            )
        )
    }

    /**
     * We want to make sure that home is swipeUpAble
     * We want to make sure that the home recommendation tab:
     * - Is showing all the components when the recom viewholder is binded
     */
    fun assertHomeScrollingAndRecomTab(activity: InstrumentationHomeRevampTestActivity) {
        /**
         * Assert home content to match given mock value
         */
        val homeRecyclerView = activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        while (homeRecyclerView?.canScrollVertically(1) == true) {
            Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view))
                .perform(ViewActions.swipeUp())
        }
        Espresso.onView(ViewMatchers.withId(R.id.view_feed_shadow))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tab_layout_home_feeds))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.view_pager_home_feeds))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun assertCoachmarkAndNext(
        titleRes: Int? = null,
        descRes: Int? = null,
        title: String? = null,
        desc: String? = null
    ) {
        Thread.sleep(1000)
        titleRes?.let {
            Espresso.onView(ViewMatchers.withText(titleRes))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        descRes?.let {
            Espresso.onView(ViewMatchers.withText(descRes))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        title?.let {
            Espresso.onView(ViewMatchers.withText(title))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        desc?.let {
            Espresso.onView(ViewMatchers.withText(desc))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        Espresso.onView(ViewMatchers.withId(R.id.step_next))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click())
    }
}
