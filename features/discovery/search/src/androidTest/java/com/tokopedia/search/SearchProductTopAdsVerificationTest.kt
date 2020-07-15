package com.tokopedia.search

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.database.STATUS_MATCH
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.data.repository.TopAdsLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsLogDBSource
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsVerificationNetworkSource
import com.tokopedia.analyticsdebugger.debugger.domain.GetTopAdsLogDataUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import com.tokopedia.usecase.RequestParams
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TOPADS_URL_TYPE_CLICK = "click"
private const val TOPADS_URL_TYPE_IMPRESSION = "impression"

internal class SearchProductTopAdsVerficationTest {

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val topAdsLogDBSource = TopAdsLogDBSource(context)
    private val topAdsImpressionUrlList = mutableSetOf<String>()
    private val topAdsClickUrlList = mutableSetOf<String>()

    @Before
    fun setUp() {
        topAdsLogDBSource.deleteAll().subscribe()

        disableOnBoarding(context)

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)

        activityRule.activity.finish()

        topAdsLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testTopAdsUrlTracking() {
        performUserJourney()
        assertTracking()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val productListAdapter = recyclerView.getProductListAdapter()
        productListAdapter.itemList.forEachIndexed { index, visitable ->
            if (visitable is ProductItemViewModel && visitable.isTopAdsOrOrganicAds()) {
                topAdsImpressionUrlList.add(visitable.topadsImpressionUrl)
                topAdsClickUrlList.add(visitable.topadsClickUrl)

                onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(index, click()))
            }
        }
    }

    private fun ProductItemViewModel.isTopAdsOrOrganicAds() = isTopAds || isOrganicAds

    private fun assertTracking() {
        waitForVerification()

        val topAdsLogDBList = getVerifiedTopAdsLog()

        topAdsImpressionUrlList.assertEachTopAdsUrl(TOPADS_URL_TYPE_IMPRESSION, topAdsLogDBList)
        topAdsClickUrlList.assertEachTopAdsUrl(TOPADS_URL_TYPE_CLICK, topAdsLogDBList)
    }

    private fun waitForVerification() {
        Thread.sleep((5.5 * 60 * 1000).toLong())
    }

    private fun getVerifiedTopAdsLog(): List<TopAdsLogDB> {
        val topAdsLogDBSource = TopAdsLogDBSource(context)
        val graphqlUseCase = GraphqlUseCase()
        val topAdsVerificationNetworkSource = TopAdsVerificationNetworkSource(context, graphqlUseCase)

        val topAdsLogLocalRepository = TopAdsLogLocalRepository(topAdsLogDBSource, topAdsVerificationNetworkSource)
        val getTopAdsLogDataUseCase = GetTopAdsLogDataUseCase(topAdsLogLocalRepository)

        val requestParams = RequestParams.create()
        requestParams.putString(AnalyticsDebuggerConst.ENVIRONMENT, AnalyticsDebuggerConst.ENVIRONMENT_TEST)

        return getTopAdsLogDataUseCase.getData(requestParams)
    }

    private fun Set<String>.assertEachTopAdsUrl(type: String, topAdsLogDBList: List<TopAdsLogDB>) {
        forEach { url ->
            val topAdsLog = topAdsLogDBList.findUrl(url)

            assertEquals(type, topAdsLog.eventType)
            assertEquals(STATUS_MATCH, topAdsLog.eventStatus)
        }
    }

    private fun List<TopAdsLogDB>.findUrl(url: String): TopAdsLogDB {
        return find { it.url == url }
                ?: throw AssertionError("Unable to find url in the TopAdsLogDB. Url = $url")
    }
}