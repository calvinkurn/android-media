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
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.default_home_dc.ErrorPromptViewHolder
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeDynamicChannelErrorResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_ATF_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_ERROR_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_HEADER_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.home.ui.HomeMockValueHelper.setupDynamicChannelQueryRemoteConfig
import com.tokopedia.home.ui.HomeMockValueHelper.setupRemoteConfig
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
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
class HomeFragmentDynamicChannelErrorUiTest {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val totalData =
        MOCK_HEADER_COUNT + MOCK_ATF_COUNT + MOCK_DYNAMIC_CHANNEL_ERROR_COUNT + MOCK_RECOMMENDATION_TAB_COUNT

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationRegistry.getInstrumentation().context.deleteHomeDatabase()
            InstrumentationAuthHelper.clearUserSession()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeDynamicChannelErrorResponseConfig())
            disableCoachMark(context)
            setupAbTestRemoteConfig()
            setupRemoteConfig()
            setupDynamicChannelQueryRemoteConfig()
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
    fun testDynamicChannelError() {
        assertDynamicChannelError()
    }

    /**
     * We want to make sure that the home recyclerview:
     * - Showing data equally with given mock response and the static data (header + recom)
     */
    private fun assertDynamicChannelError() {
        /**
         * Assert home content to match given mock value with dynamic channel content error
         */
        Thread.sleep(5000)

        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemCountAssertion(
                totalData
            )
        )

        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemTypeAssertion(
                itemViewType = ErrorPromptViewHolder.LAYOUT,
                position = 6
            )
        )
        onView(withId(R.id.home_fragment_recycler_view)).check(
            CommonAssertion.RecyclerViewItemTypeAssertion(
                itemViewType = DynamicLegoBannerViewHolder.LAYOUT,
                position = 7
            )
        )
    }
}