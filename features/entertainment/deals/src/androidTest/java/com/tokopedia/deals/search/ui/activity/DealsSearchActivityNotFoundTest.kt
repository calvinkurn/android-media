package com.tokopedia.deals.search.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.ui.activity.mock.DealsSearchMockResponse
import com.tokopedia.deals.search.ui.activity.mock.DealsSearchNotFoundMockResponse
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * @author by abrar on 30/09/20
 */
class DealsSearchActivityNotFoundTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

    private val query = "tes"

    @get: Rule
    var activityRule: IntentsTestRule<DealsSearchActivity> = object : IntentsTestRule<DealsSearchActivity>(DealsSearchActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDbSource.deleteAll().subscribe()
            setupGraphqlMockResponse(DealsSearchNotFoundMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsSearchActivity.getCallingIntent(context)
        }
    }

    @Test
    fun testSearchFlow() {
        Thread.sleep(3000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(query), ViewActions.closeSoftKeyboard())
        Thread.sleep(2000)
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE),
                hasAllSuccess())
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE = "tracker/entertainment/deals/deals_search_not_found_tracking.json"
    }
}