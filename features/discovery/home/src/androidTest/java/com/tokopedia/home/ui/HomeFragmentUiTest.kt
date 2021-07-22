package com.tokopedia.home.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.component.enableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_ATF_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_HEADER_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.test.application.espresso_component.CommonAssertion
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 02/07/21.
 */
class HomeFragmentUiTest {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val totalData =
        MOCK_HEADER_COUNT + MOCK_ATF_COUNT + MOCK_DYNAMIC_CHANNEL_COUNT + MOCK_RECOMMENDATION_TAB_COUNT

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            gtmLogDBSource.deleteAll().subscribe()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            enableCoachMark(context)
            setupAbTestRemoteConfig()
            super.beforeActivityLaunched()
        }
    }

    @Before
    fun setupEnvironment() {
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(
            recyclerView = recyclerView,
            limitCountToIdle = totalData
        )
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
        activityRule.deleteHomeDatabase()
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    @Test
    fun testFirstTimeLoggedInUser() {
        /**
         * Onboarding and coachmark for new user
         */
        assertNavigationBottomSheetDisplayed()
        assertHomeCoachmarkDisplayed()

        /**
         * Home skeleton content
         * - Toolbar
         * - Header + Balance Widget
         * - Home Content (Header + ATF + BTF)
         * - Recommendation tab / Mega feed
         */
        assertNavigationToolbar()
        assertHeader()
        assertHomeContent()
        assertHomeInteractionAndRecomTab()
    }

    /**
     * We want to make sure that the navigation toolbar:
     * - Showing correct icon for homepage (MESSAGE, CART, and NAVIGATION)
     * - Showing correct notification counter based on home mock response
     * (GLOBAL NAV only showing dot icon)
     */
    private fun assertNavigationToolbar() {
        onView(withTagStringValue(IconList.NAME_MESSAGE)).check(matches(isDisplayed()))
        onView(withTagStringValue(HomeTagHelper.getNotifCounterMessage(context))).check(
            matches(
                isDisplayed()
            )
        )
        onView(withTagStringValue(HomeTagHelper.getNotifCounterMessage(context))).check(
            matches(
                withText(HomeMockValueHelper.DEFAULT_COUNTER_NOTIF_VALUE)
            )
        )

        onView(withTagStringValue(IconList.NAME_CART)).check(matches(isDisplayed()))
        onView(withTagStringValue(HomeTagHelper.getNotifCounterCart(context))).check(
            matches(
                isDisplayed()
            )
        )
        onView(withTagStringValue(HomeTagHelper.getNotifCounterCart(context))).check(
            matches(
                withText(HomeMockValueHelper.DEFAULT_COUNTER_NOTIF_VALUE)
            )
        )

        onView(withTagStringValue(IconList.NAME_NAV_GLOBAL)).check(matches(isDisplayed()))
        onView(withTagStringValue(HomeTagHelper.getNotifGlobalNav(context))).check(
            matches(
                isDisplayed()
            )
        )

        onView(
            withTagStringValue(
                context.getString(R.string.tag_navigation_toolbar_searchbar)
            )
        ).check(matches(isDisplayed()))
    }

    /**
     * We want to make sure that the home header:
     * - Showing header background image
     * - Showing balance adapter with 4 type (OVO, Coupon, BBO, and Rewards, based on mock response)
     * - All balance items are clickable
     */
    private fun assertHeader() {
        /**
         * Assert header background
         */
        onView(withId(R.id.view_background_image)).check(matches(isDisplayed()))

        /**
         * Assert choose address widget
         */
        onView(withId(R.id.widget_choose_address)).check(matches(isDisplayed()))

        /**
         * Assert balance widget
         */
        onView(
            withTagStringValue(
                HomeTagHelper.getGopayBalanceWidgetTag(context)
            )
        ).check(matches(isDisplayed()))
        onView(
            withTagStringValue(
                HomeTagHelper.getGopayBalanceWidgetTag(context)
            )
        ).check(matches(isClickable()))

        onView(
            withTagStringValue(
                HomeTagHelper.getBBOBalanceWidgetTag(context)
            )
        ).check(matches(isDisplayed()))
        onView(
            withTagStringValue(
                HomeTagHelper.getBBOBalanceWidgetTag(context)
            )
        ).check(matches(isClickable()))

        onView(
            withTagStringValue(
                HomeTagHelper.getCouponBalanceWidgetTag(context)
            )
        ).check(matches(isDisplayed()))
        onView(
            withTagStringValue(
                HomeTagHelper.getCouponBalanceWidgetTag(context)
            )
        ).check(matches(isClickable()))

        onView(
            withTagStringValue(
                HomeTagHelper.getTokopointBalanceWidgetTag(context)
            )
        ).check(matches(isDisplayed()))
        onView(
            withTagStringValue(
                HomeTagHelper.getTokopointBalanceWidgetTag(context)
            )
        ).check(matches(isClickable()))
    }

    /**
     * We want to make sure that the home recyclerview:
     * - Showing data equally with given mock response and the static data (header + recom)
     */
    private fun assertHomeContent() {
        /**
         * Assert home content to match given mock value
         */
        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.home_fragment_recycler_view)).check(
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
    private fun assertHomeInteractionAndRecomTab() {
        /**
         * Assert home content to match given mock value
         */
        val homeRecyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        while (homeRecyclerView?.canScrollVertically(1) == true) {
            onView(withId(R.id.home_fragment_recycler_view)).perform(swipeUp())
        }

        onView(withId(R.id.recom_divider_1)).check(matches(isDisplayed()))
        onView(withId(R.id.recom_divider_2)).check(matches(isDisplayed()))
        onView(withId(R.id.recom_divider_3)).check(matches(isDisplayed()))
        onView(withId(R.id.view_feed_shadow)).check(matches(isDisplayed()))

        onView(withId(R.id.tab_layout_home_feeds)).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager_home_feeds)).check(matches(isDisplayed()))
    }

    /**
     * Assert bottomsheet text and proceed
     */
    private fun assertNavigationBottomSheetDisplayed() {
        onView(withText(R.string.onboarding_navigation_title)).check(matches(isDisplayed()))
        onView(withText(R.string.onboarding_navigation_description)).check(matches(isDisplayed()))
        onView(withText(R.string.onboarding_navigation_button)).check(matches(isDisplayed()))
            .perform(click())
    }

    /**
     * Assert coachmark text and proceed
     */
    private fun assertHomeCoachmarkDisplayed() {
        assertCoachmarkAndNext(
            titleRes = R.string.onboarding_coachmark_inbox_title,
            descRes = R.string.onboarding_coachmark_inbox_description
        )

        assertCoachmarkAndNext(
            title = ChooseAddressUtils.coachMark2Item(context, View(context)).title.toString(),
            desc = ChooseAddressUtils.coachMark2Item(context, View(context)).description.toString()
        )

        assertCoachmarkAndNext(
            titleRes = R.string.onboarding_coachmark_wallet_title,
            descRes = R.string.onboarding_coachmark_wallet_description
        )

        assertCoachmarkAndNext(
            titleRes = R.string.home_gopay_coachmark_title,
            descRes = R.string.home_gopay_coachmark_description
        )

        assertCoachmarkAndNext(
            titleRes = R.string.home_gopay2_coachmark_title,
            descRes = R.string.home_gopay2_coachmark_description
        )

        assertCoachmarkAndNext(
            titleRes = R.string.onboarding_coachmark_title,
            descRes = R.string.onboarding_coachmark_description
        )
    }

    private fun assertCoachmarkAndNext(
        titleRes: Int? = null,
        descRes: Int? = null,
        title: String? = null,
        desc: String? = null
    ) {
        Thread.sleep(1000)
        titleRes?.let {
            onView(withText(titleRes))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        descRes?.let {
            onView(withText(descRes))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        title?.let {
            onView(withText(title))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        desc?.let {
            onView(withText(desc))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        onView(withId(R.id.step_next))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
    }
}