package com.tokopedia.deals.brand.ui.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.DealsDummyResponseString
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_RESPONSE_SECOND_CATEGORY_TITLE
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_USER_TYPE_STRING
import com.tokopedia.deals.R
import com.tokopedia.deals.category.ui.activity.mock.DealsCategoryMockResponse
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class DealsBrandsActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)

    @get: Rule
    var activityRule: IntentsTestRule<DealsBrandActivity> = object : IntentsTestRule<DealsBrandActivity>(DealsBrandActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDbSource.deleteAll().subscribe()
            setupGraphqlMockResponse(DealsCategoryMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsBrandActivity.getCallingIntent(context, "")
        }
    }

    @Test
    fun testBrandLayout() {
        changeLocationBrandPage()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        actionOnDealsBrandViewHolder()
        clickOnRelaksasiTab()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE),
                hasAllSuccess())
    }

    private fun actionOnDealsBrandViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(DUMMY_USER_TYPE_STRING), closeSoftKeyboard())

        Thread.sleep(2000)
        onView(getElementFromMatchAtPosition(withId(R.id.brand_view_holder_layout), 0)).perform(click())
    }

    private fun clickOnRelaksasiTab() {
        Thread.sleep(5000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText(DUMMY_RESPONSE_SECOND_CATEGORY_TITLE))).perform(click())
    }

    private fun changeLocationBrandPage() {
        onView(withId(R.id.txtDealsBaseLocationTitle)).perform(click())
        Thread.sleep(2000)
        onView(CommonMatcher.firstView(withText(DealsDummyResponseString.DUMMY_LOCATION_ONE_STRING))).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.txtDealsBaseLocationTitle)).perform(click())
        Thread.sleep(2000)
        onView(CommonMatcher.firstView(withText(DealsDummyResponseString.DUMMY_LOCATION_TWO_STRING))).perform(click())
        Thread.sleep(2000)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE = "tracker/entertainment/deals/deals_brand_tracking.json"
    }
}