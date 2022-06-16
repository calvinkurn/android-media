package com.tokopedia.homenav.component

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.homenav.mock.MainNavMockResponseConfig
import com.tokopedia.homenav.util.MainNavRecyclerViewIdlingResource
import com.tokopedia.homenav.view.activity.HomeNavActivity
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by dhaba
 */
private const val TAG = "HomeNavAnalyticsTest"
@CassavaTest
class HomeNavAnalyticTest {
    @get:Rule
    var activityRule = object: IntentsTestRule<HomeNavActivity>(
        HomeNavActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(MainNavMockResponseConfig())
            setupAbTestRemoteConfig()
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private var mainNavRecyclerViewIdlingResource: MainNavRecyclerViewIdlingResource? = null

    @Before
    fun resetAll() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val recyclerView: RecyclerView =
                activityRule.activity.findViewById(com.tokopedia.homenav.R.id.recycler_view)
        mainNavRecyclerViewIdlingResource = MainNavRecyclerViewIdlingResource(
            recyclerView = recyclerView
        )
        IdlingRegistry.getInstance().register(mainNavRecyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(mainNavRecyclerViewIdlingResource)
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        login()
        waitForData()
    }

    @Test
    fun testComponentFavoriteShop() {
        initTest()
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            RollenceKey.ME_PAGE_REVAMP,
            RollenceKey.ME_PAGE_REVAMP_VARIANT
        )
    }
}