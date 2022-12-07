package com.tokopedia.homenav.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.homenav.mock.MainNavMockResponseConfig
import com.tokopedia.homenav.test.R
import com.tokopedia.homenav.util.MainNavRecyclerViewIdlingResource
import com.tokopedia.homenav.view.activity.HomeNavActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 02/07/21.
 */
class MainNavFragmentNonLogin {
    private var mainNavRecyclerViewIdlingResource: MainNavRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule = object : ActivityTestRule<HomeNavActivity>(
        HomeNavActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            setupGraphqlMockResponse(MainNavMockResponseConfig())
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
    fun testMainNavNonLogin() {
        /**
         * Main nav component
         * - Toolbar
         * - Account header
         */
        assertNavigationToolbar()

        /**
         * Main nav in login state will showing sign up or sign in button
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
     * Gopay eligible account data:
     * - user profile
     * - user wallet - gopay
     * - user saldo
     * - user shop / seller info
     */
    private fun assertAccountHeader() {
        /**
         * Assert button login and button signup
         */
        onView(withId(R.id.layout_nonlogin)).check(matches(
            allOf(isDisplayed())
        ))
        onView(withId(R.id.btn_register)).check(matches(
            allOf(isDisplayed(), isClickable())
        ))
        onView(withId(R.id.btn_login)).check(matches(
            allOf(isDisplayed(), isClickable())
        ))

        /**
         * Assert profile user section not showing
         */
        onView(withId(R.id.layout_login)).check(matches(
            allOf(not(isDisplayed()))
        ))
    }
}
