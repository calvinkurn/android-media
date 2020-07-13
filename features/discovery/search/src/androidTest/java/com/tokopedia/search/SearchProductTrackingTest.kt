package com.tokopedia.search

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.*
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.constants.SearchConstant.FreeOngkir.FREE_ONGKIR_LOCAL_CACHE_NAME
import com.tokopedia.discovery.common.constants.SearchConstant.FreeOngkir.FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.FILTER_ONBOARDING_SHOWN
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.LOCAL_CACHE_NAME
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/search/search_product.json"
private const val TAG = "SearchProductTest"

internal class SearchProductTrackingTest {

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Test
    fun testTrackingUsingMockData() {
        setupGraphqlMockResponse()

        testTracking()
    }

    @Test
    @Ignore("Ignore for testing with real data.")
    fun testTrackingUsingRealData() {
        testTracking()
    }

    private fun setupGraphqlMockResponse() {
        val mockModelConfig = createMockModelConfig()
        mockModelConfig.createMockModel(context)

        val testInterceptors = listOf(MockInterceptor(mockModelConfig))

        GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
    }

    private fun createMockModelConfig(): MockModelConfig {
        val mapMockResponse = createMapOfMockResponse()

        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                for ((key, value) in mapMockResponse.entries) addMockResponse(key, value, FIND_BY_CONTAINS)
                return this
            }
        }
    }

    private fun createMapOfMockResponse() = mapOf(
            "SearchProduct" to getRawString(context, com.tokopedia.search.test.R.raw.search_product_common_response)
    )

    private fun testTracking() {
        setUp()

        performUserJourney()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess())

        tearDown()
    }

    private fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()

        disableOnBoarding()

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        intending(isInternal()).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    private fun disableOnBoarding() {
        LocalCacheHandler(context, FREE_ONGKIR_LOCAL_CACHE_NAME).also {
            it.putBoolean(FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
            it.applyEditor()
        }

        LocalCacheHandler(context, LOCAL_CACHE_NAME).also {
            it.putBoolean(FILTER_ONBOARDING_SHOWN, true)
            it.applyEditor()
        }
    }

    private fun createIntent(): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, SearchActivity::class.java).also {
            it.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=samsung")
        }
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val productListAdapter = recyclerView.getProductListAdapter()
        val topAdsItemPosition = productListAdapter.itemList.getFirstTopAdsProductPosition()
        val organicItemPosition = productListAdapter.itemList.getFirstOrganicProductPosition()

        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(topAdsItemPosition, click()))
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(organicItemPosition, click()))

        activityRule.activity.finish()
    }

    private fun RecyclerView?.getProductListAdapter(): ProductListAdapter {
        val productListAdapter = this?.adapter as? ProductListAdapter

        if (productListAdapter == null) {
            val detailMessage = "Adapter is not ${ProductListAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return productListAdapter
    }

    private fun List<Visitable<*>>.getFirstTopAdsProductPosition(): Int {
        return indexOfFirst { it is ProductItemViewModel && it.isTopAds }
    }

    private fun List<Visitable<*>>.getFirstOrganicProductPosition(): Int {
        return indexOfFirst { it is ProductItemViewModel && !it.isTopAds }
    }

    private fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
