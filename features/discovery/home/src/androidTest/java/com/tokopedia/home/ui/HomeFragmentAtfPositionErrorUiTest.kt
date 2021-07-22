package com.tokopedia.home.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.HomeAtfErrorViewHolder
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeAtfPositionErrorResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_ATF_ERROR_POSITION_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_HEADER_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
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
class HomeFragmentAtfPositionErrorUiTest {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val totalData =
        MOCK_HEADER_COUNT + MOCK_ATF_ERROR_POSITION_COUNT + MOCK_DYNAMIC_CHANNEL_COUNT + MOCK_RECOMMENDATION_TAB_COUNT

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            gtmLogDBSource.deleteAll().subscribe()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeAtfPositionErrorResponseConfig())
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
        activityRule.deleteHomeDatabase()
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
         * Assert home content to match given mock value with atf position error
         */
        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemCountAssertion(
                totalData
            )
        )

        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemTypeAssertion(
                itemViewType = HomeAtfErrorViewHolder.LAYOUT,
                position = 1
            )
        )
    }
}