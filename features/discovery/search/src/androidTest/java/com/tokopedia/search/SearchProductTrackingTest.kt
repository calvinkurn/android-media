package com.tokopedia.search

import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcViewHolder
import com.tokopedia.search.utils.SearchIdlingResource
import com.tokopedia.search.utils.clickAddToCartOnInspirationListATC
import com.tokopedia.search.utils.clickAddToCartOnProductCard
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/search/search_product.json"
private const val ANALYTIC_VALIDATOR_QUERY_THANOS_ID = "7"

@CassavaTest
internal class SearchProductTrackingTest {

    private val isFromNetwork = true
    private val queryId
        get() =
            if (isFromNetwork) ANALYTIC_VALIDATOR_QUERY_THANOS_ID
            else ANALYTIC_VALIDATOR_QUERY_FILE_NAME

    @get:Rule
    val activityRule = IntentsTestRule(
        SearchActivity::class.java,
        false,
        false
    )

    @get:Rule
    val cassavaTestRule = CassavaTestRule(isFromNetwork)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val blockAllIntentsMonitor = Instrumentation.ActivityMonitor(
        null as String?,
        null,
        true
    )

    @Before
    fun setUp() {
        setupGraphqlMockResponse(SearchMockModelConfig())

        disableOnBoarding(context)

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        InstrumentationRegistry.getInstrumentation().addMonitor(blockAllIntentsMonitor)
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        IdlingRegistry.getInstance().register(SearchIdlingResource.idlingResource)
    }

    @Test
    fun testTracking() {
        performUserJourney()

        assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val productListAdapter = recyclerView.getProductListAdapter()
        val topAdsPosition = productListAdapter.itemList.getFirstTopAdsProductPosition()
        val organicPosition = productListAdapter.itemList.getFirstOrganicProductPosition()
        val inspirationListATCPosition =
            productListAdapter.itemList.indexOfFirst { it is InspirationListAtcDataView }

        recyclerView.perform(
            actionOnItemAtPosition<ProductItemViewHolder>(
                topAdsPosition,
                clickAddToCartOnProductCard()
            )
        )

        recyclerView.perform(
            actionOnItemAtPosition<ProductItemViewHolder>(
                organicPosition,
                clickAddToCartOnProductCard()
            )
        )

        recyclerView.perform(
            actionOnItemAtPosition<InspirationListAtcViewHolder>(
                inspirationListATCPosition,
                clickAddToCartOnInspirationListATC()
            )
        )

        activityRule.activity.finish()

        // Wait for tracking queue
        Thread.sleep(1_000)
    }

    @After
    fun tearDown() {
        InstrumentationRegistry.getInstrumentation().removeMonitor(blockAllIntentsMonitor)
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
        IdlingRegistry.getInstance().unregister(SearchIdlingResource.idlingResource)
    }
}
