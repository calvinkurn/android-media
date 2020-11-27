package com.tokopedia.travelhomepage.destination.presentation.activity

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.*
import com.tokopedia.travelhomepage.R

/**
 * @author by jessica on 26/11/20
 */
class TravelDestinationActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule: IntentsTestRule<TravelDestinationActivity> = object : IntentsTestRule<TravelDestinationActivity>(TravelDestinationActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            graphqlCacheManager.deleteAll()
            gtmLogDBSource.deleteAll().toBlocking().first()
            setupGraphqlMockResponse {
                addMockResponse(KEY_TRAVEL_DESTINATION_CITY_DATA, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_city_data), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_RECOMMENDATION, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_recommendation), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_SUMMARY, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_summary), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_ORDER_LIST, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_order_list), MockModelConfig.FIND_BY_CONTAINS)

                addMockResponse(KEY_TRAVEL_DESTINATION_ARTICLE, InstrumentationMockHelper.getRawString(context,
                        com.tokopedia.travelhomepage.test.R.raw.response_mock_travel_destination_city_article), MockModelConfig.FIND_BY_CONTAINS)
            }
        }

        override fun getActivityIntent(): Intent {
            return TravelDestinationActivity.createInstance(context, DUMMY_WEB_URL)
        }
    }

    @Before
    fun setUp() {
    }

    @Test
    fun mainFlow() {
        Thread.sleep(5000)

        Espresso.onView(withId(R.id.scroll_view)).perform(ViewActions.swipeUp())

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_TRAVEL_DESTINATION),
                hasAllSuccess())
    }

    @After
    fun tearDown() {
    }

    companion object {
        const val KEY_TRAVEL_DESTINATION_RECOMMENDATION = "TravelCollectiveRecommendation"
        const val KEY_TRAVEL_DESTINATION_SUMMARY = "TravelDestinationSummary"
        const val KEY_TRAVEL_DESTINATION_CITY_DATA = "TravelDestinationCityData"
        const val KEY_TRAVEL_DESTINATION_ORDER_LIST = "TravelCollectiveOrderList"
        const val KEY_TRAVEL_DESTINATION_ARTICLE = "TravelArticle"

        const val DUMMY_WEB_URL = "https://m.tokopedia.com/travel-entertainment/bandung/"

        const val ANALYTIC_VALIDATOR_QUERY_TRAVEL_DESTINATION = "tracker/travel/homepage/travel_destination.json"

    }
}