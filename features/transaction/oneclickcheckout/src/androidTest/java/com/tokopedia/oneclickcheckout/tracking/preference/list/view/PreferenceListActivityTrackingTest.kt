package com.tokopedia.oneclickcheckout.tracking.preference.list.view

import android.app.Instrumentation
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.GET_PREFERENCE_LIST_DEFAULT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_PREFERENCE_LIST_QUERY
import com.tokopedia.oneclickcheckout.common.interceptor.SET_DEFAULT_PROFILE_DEFAULT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.SET_DEFAULT_PROFILE_QUERY
import com.tokopedia.oneclickcheckout.common.robot.preferenceListPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreferenceListActivityTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_preference_list.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule(PreferenceListActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val cls: String? = null
    private val activityMonitor = Instrumentation.ActivityMonitor(cls, null, true)

    private var idlingResource: IdlingResource? = null

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    private fun setupGraphqlMockResponse() {
        val mockModelConfig = createMockModelConfig()
        mockModelConfig.createMockModel(context)

        val testInterceptors = listOf(MockInterceptor(mockModelConfig))

        GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse(GET_PREFERENCE_LIST_QUERY, getJsonFromResource(GET_PREFERENCE_LIST_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                addMockResponse(SET_DEFAULT_PROFILE_QUERY, getJsonFromResource(SET_DEFAULT_PROFILE_DEFAULT_RESPONSE_PATH), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse()

        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        activityRule.launchActivity(null)

        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor)
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(idlingResource)

        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)
    }

    @Test
    fun performPreferenceListTrackingActions() {
        preferenceListPage {
            clickAddPreference()

            clickEditPreference(0)

            val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.rv_preference_list).adapter!!.itemCount
            for (i in 0 until itemCount) {
                chooseDefaultPreference(i)
            }
        }

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}
