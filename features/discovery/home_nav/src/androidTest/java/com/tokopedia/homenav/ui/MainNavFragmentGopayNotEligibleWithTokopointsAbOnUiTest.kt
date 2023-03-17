package com.tokopedia.homenav.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.homenav.mock.MainNavMockNotEligibleResponseConfig
import com.tokopedia.homenav.test.R
import com.tokopedia.homenav.ui.MainNavMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.homenav.util.MainNavRecyclerViewIdlingResource
import com.tokopedia.homenav.view.activity.HomeNavActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 02/07/21.
 */
class MainNavFragmentGopayNotEligibleWithTokopointsAbOnUiTest {
    private var mainNavRecyclerViewIdlingResource: MainNavRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule = object : ActivityTestRule<HomeNavActivity>(
        HomeNavActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(MainNavMockNotEligibleResponseConfig())
            setupAbTestRemoteConfig()
            super.beforeActivityLaunched()
        }
    }

    @Before
    fun setupEnvironment() {
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(R.id.recycler_view)
        mainNavRecyclerViewIdlingResource = MainNavRecyclerViewIdlingResource(
            recyclerView = recyclerView
        )
        IdlingRegistry.getInstance().register(mainNavRecyclerViewIdlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(mainNavRecyclerViewIdlingResource)
    }

    @Test
    fun testMainNavWithGopayNotEligible_and_tokopointsAbOn() {
        /**
         * Main nav component
         * - Toolbar
         * - Account header
         */
        assertNavigationToolbar()

        /**
         * Main nav with gopay not eligible and tokopoints ab on
         * will showing tokopoints
         */
        assertAccountHeader()
    }

    /**
     * We want to make sure that the navigation toolbar:
     * - Showing correct icon for mainnav (CLOSE)
     * - Showing correct title
     */
    private fun assertNavigationToolbar() {
        onView(withId(R.id.toolbar_title)).check(matches(
            allOf(
                isDisplayed(),
                withText(
                    context.getString(R.string.title_main_nav)
                )
            )
        ))
        onView(withId(R.id.nav_icon_back)).check(matches(
            allOf(
                isDisplayed()
            )
        ))
    }

    /**
     * Wallet not eligible and tokopoints on account data:
     * - user profile
     * - user wallet - tokopoints
     * - user saldo
     * - user shop / seller info
     */
    private fun assertAccountHeader() {
        /**
         * Assert profile user section
         */
        onView(withId(R.id.usr_badge)).check(matches(
            allOf(isDisplayed())
        ))

        onView(withId(R.id.tv_name)).check(matches(
            allOf(
                isDisplayed(),
                withText(MainNavMockValueHelper.MOCK_VALUE_USERNAME)
            )
        ))

        /**
         * Assert profile wallet section
         */
        onView(withId(R.id.usr_ovo_badge)).check(matches(
            allOf(
                isDisplayed()
            )
        ))

        onView(withId(R.id.tv_ovo)).check(matches(
            allOf(
                isDisplayed(),
                withText(
                    String.format(
                        context.getString(R.string.mainnav_tokopoint_format),
                        MainNavMockValueHelper.MOCK_VALUE_TOKOPOINTS_EXTERNAL_AMOUNT,
                        MainNavMockValueHelper.MOCK_VALUE_TOKOPOINTS_AMOUNT
                    )
                )
                )
        ))

        /**
         * Assert profile wallet section
         */
        onView(withId(R.id.usr_saldo_badge)).check(matches(
            allOf(
                isDisplayed()
            )
        ))

        onView(withId(R.id.tv_saldo)).check(matches(
            allOf(
                isDisplayed(),
                withText(
                    MainNavMockValueHelper.MOCK_VALUE_SALDO
                )
            )
        ))

        /**
         * Assert shop info section
         */
        onView(withId(R.id.usr_shop_info)).check(matches(
            allOf(
                isDisplayed(),
                withText(
                    MainNavMockValueHelper.MOCK_VALUE_SHOP_NAME
                )
            )
        ))

        onView(withId(R.id.usr_shop_notif)).check(matches(
            allOf(
                isDisplayed(),
                withText(
                    MainNavMockValueHelper.MOCK_VALUE_SHOP_NOTIF
                )
            )
        ))
    }
}
