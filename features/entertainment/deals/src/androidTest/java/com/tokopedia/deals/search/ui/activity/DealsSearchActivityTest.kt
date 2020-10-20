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
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.ui.activity.mock.DealsSearchMockResponse
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
class DealsSearchActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

    private val query = "tes"

    @Inject
    lateinit var dealsLocationUtils: DealsLocationUtils

    @get: Rule
    var activityRule: IntentsTestRule<DealsSearchActivity> = object : IntentsTestRule<DealsSearchActivity>(DealsSearchActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDbSource.deleteAll().subscribe()
            setupGraphqlMockResponse(DealsSearchMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsSearchActivity.getCallingIntent(context)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testSearchFlow() {
        //changeLocation()
        actionOnLastSeen()
        actionOnCuratedViewHolder()
        actionOnVoucherViewHolder()
        actionOnMerchantViewHolder()
        impressionNotFoundViewHolder()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE),
                hasAllSuccess())
    }

    private fun changeLocation () {
        Thread.sleep(2000)
        val id = dealsLocationUtils.getLocation().id++
        dealsLocationUtils.updateLocation(Location(id))
    }

    private fun actionOnVoucherViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(query), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.voucher_deals_layout), 1)).perform(click())
    }

    private fun actionOnMerchantViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(query), ViewActions.closeSoftKeyboard())

        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.brand_view_holder_layout), 1)).perform(click())
    }

    private fun actionOnLastSeen() {
        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.curated_layout), 0)).perform(click())
    }

    private fun actionOnCuratedViewHolder() {
        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.curated_layout), 1)).perform(click())
    }


    private fun impressionNotFoundViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(typeText("tesasjsagsja"), ViewActions.closeSoftKeyboard())
        Thread.sleep(2000)
        onView(getElementFromMatchAtPosition(withId(R.id.not_found_layout), 1))
    }


    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_SEARHPAGE = "tracker/entertainment/deals/deals_search_tracking.json"
    }
}