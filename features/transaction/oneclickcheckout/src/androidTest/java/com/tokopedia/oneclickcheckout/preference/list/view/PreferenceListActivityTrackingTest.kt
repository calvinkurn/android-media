package com.tokopedia.oneclickcheckout.preference.list.view

import android.app.Instrumentation
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.Status
import com.tokopedia.analyticsdebugger.validator.core.assertAnalyticWithValidator
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PreferenceListActivityTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_preference_list.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule(PreferenceListActivity::class.java, false, false)

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

    private fun setup() {
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse()

        activityRule.launchActivity(null)

        idlingResource = SwipeRefreshIdlingResource(activityRule.activity, R.id.swipe_refresh_layout)
        IdlingRegistry.getInstance().register(idlingResource)

        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor)
    }

    private fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)

        InstrumentationRegistry.getInstrumentation().removeMonitor(activityMonitor)

        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testTracking() {
        setup()

        // perform click add button
        Espresso.onView(ViewMatchers.withId(R.id.btn_preference_list_action)).perform(click())

        // perform click gear
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "perform click on first profile checkbox"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<ImageView>(R.id.iv_edit_preference).callOnClick()
            }
        }))

        // perform change default profile
        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.rv_preference_list).adapter!!.itemCount
        for (i in 0 until itemCount) {
            Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(i, object : ViewAction {
                override fun getDescription(): String = "perform click on first profile checkbox"

                override fun getConstraints(): Matcher<View>? = null

                override fun perform(uiController: UiController?, view: View) {
                    view.findViewById<CheckboxUnify>(R.id.cb_main_preference).callOnClick()
                }
            }))
        }

        assertAnalyticWithValidator(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME) {
            assertEquals(Status.SUCCESS, it.status)
        }

        cleanup()
    }
}
