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
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.default_home_dc.ErrorPromptViewHolder
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeDynamicChannelErrorResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_ATF_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_ERROR_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_HEADER_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.CHOOSE_ADDRESS_ROLLENCE_KEY
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.BALANCE_EXP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.BALANCE_VARIANT_NEW
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.HOME_EXP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.HOME_VARIANT_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.KEY_AB_INBOX_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.VARIANT_NEW_INBOX
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
class HomeFragmentDynamicChannelErrorUiTest {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            gtmLogDBSource.deleteAll().subscribe()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeDynamicChannelErrorResponseConfig())
            disableCoachMark(context)
            setupAbTestRemoteConfig()
            super.beforeActivityLaunched()
        }
    }

    @Test
    fun testDynamicChannelError() {
        assertDynamicChannelError()
    }

    @Before
    fun setupIdlingResource() {
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(recyclerView)
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    /**
     * We want to make sure that the home recyclerview:
     * - Showing data equally with given mock response and the static data (header + recom)
     */
    private fun assertDynamicChannelError() {
        /**
         * Assert home content to match given mock value with dynamic channel content error
         */
        val totalData =
            MOCK_HEADER_COUNT + MOCK_ATF_COUNT + MOCK_DYNAMIC_CHANNEL_ERROR_COUNT + MOCK_RECOMMENDATION_TAB_COUNT
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

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            KEY_AB_INBOX_REVAMP,
            VARIANT_NEW_INBOX
        )
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            NAVIGATION_EXP_TOP_NAV,
            NAVIGATION_VARIANT_REVAMP
        )
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            CHOOSE_ADDRESS_ROLLENCE_KEY,
            CHOOSE_ADDRESS_ROLLENCE_KEY
        )
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            BALANCE_EXP,
            BALANCE_VARIANT_NEW
        )
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            HOME_EXP,
            HOME_VARIANT_REVAMP
        )
    }
}