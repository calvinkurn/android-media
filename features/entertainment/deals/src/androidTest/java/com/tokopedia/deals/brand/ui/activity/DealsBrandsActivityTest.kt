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
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.DealsDummyResponseString
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_RESPONSE_SECOND_CATEGORY_TITLE
import com.tokopedia.deals.DealsDummyResponseString.DUMMY_USER_TYPE_STRING
import com.tokopedia.deals.R
import com.tokopedia.deals.brand_detail.ui.activity.DealsBrandDetailActivity
import com.tokopedia.deals.category.ui.activity.mock.DealsCategoryMockResponse
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBrandsActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule =
        ActivityTestRule(DealsBrandActivity::class.java, false, false)

    @Before
    fun setup(){
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDbSource.deleteAll().subscribe()
        setupGraphqlMockResponse(DealsCategoryMockResponse())
        val intent = DealsBrandActivity.getCallingIntent(context, "")
        activityRule.launchActivity(intent)
    }


    @Test
    fun testBrandLayout() {
        changeLocationBrandPage()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        actionOnDealsBrandViewHolder()
        clickOnRelaksasiTab()

        Assert.assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE),
                hasAllSuccess())

    }

    private fun actionOnDealsBrandViewHolder() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click()).perform(typeText(DUMMY_USER_TYPE_STRING), closeSoftKeyboard())

        Thread.sleep(2000)
        onView(getElementFromMatchAtPosition(withId(R.id.brand_view_holder_layout), 0)).perform(click())
    }

    private fun clickOnRelaksasiTab() {
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText(DUMMY_RESPONSE_SECOND_CATEGORY_TITLE))).perform(click())
        activityRule.finishActivity()
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

    @After
    fun tearDown() {
        gtmLogDbSource.deleteAll().subscribe()
        Intents.release()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_BRANDPAGE = "tracker/entertainment/deals/deals_brand_tracking.json"
    }
}