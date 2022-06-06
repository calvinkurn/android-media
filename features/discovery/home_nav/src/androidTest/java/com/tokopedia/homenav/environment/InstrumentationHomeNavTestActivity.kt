package com.tokopedia.homenav.environment

import android.app.Activity
import android.app.Instrumentation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.homenav.view.activity.HomeNavPerformanceInterface
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.homenav.mock.MainNavMockResponseConfig
import com.tokopedia.homenav.util.MainNavRecyclerViewIdlingResource
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 * Created by dhaba
 */
@CassavaTest
class InstrumentationHomeNavTestActivity : AppCompatActivity(), HomeNavPerformanceInterface {
    private val navPerformanceMonitoring = PerformanceMonitoring()
    private val navPerformanceCallback = PageLoadTimePerformanceCallback(
            NAV_PAGE_PERFORMANCE_MONITORING_PREPARE_METRICS,
            NAV_PAGE_PERFORMANCE_MONITORING_NETWORK_METRICS,
            NAV_PAGE_PERFORMANCE_MONITORING_RENDER_METRICS,
            0,
            0,
            0,
            0,
            navPerformanceMonitoring
    )

    companion object {
        private const val NAV_PAGE_PERFORMANCE_MONITORING_PREPARE_METRICS = "nav_page_plt_start_page_metrics"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_NETWORK_METRICS = "nav_page_plt_network_request_page_metrics"
        private const val NAV_PAGE_PERFORMANCE_MONITORING_RENDER_METRICS = "nav_page_plt_render_page_metrics"
    }

    override fun getNavPerformanceInterface(): PageLoadTimePerformanceInterface {
        return navPerformanceCallback
    }

    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationHomeNavTestActivity>(InstrumentationHomeNavTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(MainNavMockResponseConfig())
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
}