package com.tokopedia.home.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.R
import com.tokopedia.home.component.enableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by frenzel on 11/07/23
 */
@UiTest
class HomeCommonNonLoggedInUiTest : HomeNonLoginAssertion {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val totalData =
        HomeMockValueHelper.MOCK_HEADER_COUNT + HomeMockValueHelper.MOCK_ATF_COUNT + HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_COUNT + HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationRegistry.getInstrumentation().context.deleteHomeDatabase()
            InstrumentationAuthHelper.clearUserSession()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            enableCoachMark(context)
            HomeMockValueHelper.setupAbTestRemoteConfig(atf2Rollence = false)
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
    fun testControlNonLoggedInUser() {
        /**
         * Home skeleton content for non login
         * - Toolbar
         * - Home Content (Header + ATF + BTF)
         * - Recommendation tab / Mega feed
         */
        assertNavigationToolbar(context)
        assertHomeContent(totalData)
        assertHomeScrollingAndRecomTab(activityRule.activity)
    }
}
