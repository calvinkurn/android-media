package com.tokopedia.deals.search.ui.activity

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_USER_TYPE_STRING
import com.tokopedia.deals.search.ui.activity.mock.DealsSearchNotFoundMockResponse
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * @author by abrar on 30/09/20
 */
class DealsSearchActivityNotFoundTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

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
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(DUMMY_USER_TYPE_STRING), ViewActions.closeSoftKeyboard())
        Thread.sleep(2000)
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE),
                hasAllSuccess())
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE = "tracker/entertainment/deals/deals_search_not_found_tracking.json"
    }
}