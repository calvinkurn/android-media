package com.tokopedia.home.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ErrorStateChannelOneViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.ErrorStateIconViewHolder
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeAtfContentErrorResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_ATF_ERROR_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_HEADER_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.espresso_component.CommonAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 02/07/21.
 */
@UiTest
class HomeFragmentAtfContentErrorUiTest {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val totalData =
        MOCK_HEADER_COUNT + MOCK_ATF_ERROR_COUNT + MOCK_DYNAMIC_CHANNEL_COUNT + MOCK_RECOMMENDATION_TAB_COUNT

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationRegistry.getInstrumentation().context.deleteHomeDatabase()
            InstrumentationAuthHelper.clearUserSession()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeAtfContentErrorResponseConfig())
            disableCoachMark(context)
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
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    @Test
    fun testAtfContentError() {
        assertAtfContentError()
    }

    /**
     * We want to make sure that the home recyclerview:
     * - Showing data equally with given mock response and the static data (header + recom)
     */
    private fun assertAtfContentError() {
        /**
         * Assert home content to match given mock value with atf content error
         */
        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemCountAssertion(
                totalData
            )
        )

        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemTypeAssertion(
                itemViewType = ErrorStateIconViewHolder.LAYOUT,
                position = 1
            )
        )
        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemTypeAssertion(
                itemViewType = ErrorStateChannelOneViewHolder.LAYOUT,
                position = 2
            )
        )
        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemTypeAssertion(
                itemViewType = ErrorStateIconViewHolder.LAYOUT,
                position = 3
            )
        )
    }
}