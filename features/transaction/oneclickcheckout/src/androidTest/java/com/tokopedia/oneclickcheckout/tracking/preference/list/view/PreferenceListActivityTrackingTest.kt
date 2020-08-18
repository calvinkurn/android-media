package com.tokopedia.oneclickcheckout.tracking.preference.list.view

import android.app.Instrumentation
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.GET_PREFERENCE_LIST_DEFAULT_RESPONSE
import com.tokopedia.oneclickcheckout.common.interceptor.GET_PREFERENCE_LIST_QUERY
import com.tokopedia.oneclickcheckout.common.interceptor.SET_DEFAULT_PROFILE_DEFAULT_RESPONSE
import com.tokopedia.oneclickcheckout.common.interceptor.SET_DEFAULT_PROFILE_QUERY
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListActivity
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListViewHolder
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import org.hamcrest.Matcher
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
                addMockResponse(GET_PREFERENCE_LIST_QUERY, GET_PREFERENCE_LIST_DEFAULT_RESPONSE, FIND_BY_CONTAINS)
                addMockResponse(SET_DEFAULT_PROFILE_QUERY, SET_DEFAULT_PROFILE_DEFAULT_RESPONSE, FIND_BY_CONTAINS)
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
    fun testTracking() {
        // perform click add button
        onView(withId(R.id.btn_preference_list_action)).perform(click())

        // perform click gear
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "perform click on first profile checkbox"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<ImageView>(R.id.iv_edit_preference).callOnClick()
            }
        }))

        // perform change default profile
        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.rv_preference_list).adapter!!.itemCount
        for (i in 0 until itemCount) {
            onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(i, object : ViewAction {
                override fun getDescription(): String = "perform click on first profile checkbox"

                override fun getConstraints(): Matcher<View>? = null

                override fun perform(uiController: UiController?, view: View) {
                    view.findViewById<CheckboxUnify>(R.id.cb_main_preference).callOnClick()
                }
            }))
        }

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}
