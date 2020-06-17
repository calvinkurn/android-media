package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.analyticsdebugger.validator.core.*
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.ProductListAdapter
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.schedulers.Schedulers

internal class SearchTrackingAnalyticValidatorTest {

    @get:Rule
    var activityRule = ActivityTestRule<SearchActivity>(SearchActivity::class.java, false, false)

    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null

    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(InstrumentationRegistry.getInstrumentation().targetContext) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }

    private fun setUpActivity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = createIntent(context, "samsung")
        activityRule.launchActivity(intent)
    }

    private fun createIntent(context: Context?, query: String): Intent? {
        val intent = Intent(context, SearchActivity::class.java)
        intent.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=" + query)
        return intent
    }

    @Before
    fun setUp() {
        dao.deleteAll().subscribe()
    }

    @After
    fun tearDown() {
        dao.deleteAll().subscribe()
    }

    @Test
    fun testTracking() {
        setUpActivity()

        recyclerView = activityRule.activity.findViewById<RecyclerView>(recyclerViewId).also {
            recyclerViewIdlingResource = RecyclerViewIdlingResource(it)
        }

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)

        onPageLoaded {
            performUserJourney()
            assertTracking()
        }

        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    private fun onPageLoaded(onLoaded: () -> Unit) {
        onLoaded()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val productListAdapter = recyclerView?.adapter as? ProductListAdapter ?: throw AssertionError("Adapter is not ${ProductListAdapter::class.java.simpleName}")
        val organicItemPosition = productListAdapter.itemList.indexOfFirst { it is ProductItemViewModel && !it.isTopAds }
        val topAdsItemPosition = productListAdapter.itemList.indexOfFirst { it is ProductItemViewModel && it.isTopAds }

        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(topAdsItemPosition, click()))
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(organicItemPosition, click()))

        activityRule.activity.finish()
    }

    private fun assertTracking() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val searchProductAnalyticValidatorQuery = Utils.getJsonDataFromAsset(context, "tracker/search/search_product.json")
        searchProductAnalyticValidatorQuery ?: throw AssertionError("Validator Query not found")

        val testCases = searchProductAnalyticValidatorQuery.toJsonMap().getQuery().map { it.toDefaultValidator() }
        engine.compute(testCases).test {
            it.assertSuccessEvent()
        }
    }

    private fun Map<String, Any>.getQuery(): List<Map<String, Any>> {
        return this["query"] as List<Map<String, Any>>
    }

    private fun <T> Observable<T>.test(onNext: (T) -> Unit) {
        this.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.immediate()).subscribe(onNext)
    }

    private fun List<Validator>.assertSuccessEvent() {
        forEach { it.assertSuccessEvent() }
    }

    private fun Validator.assertSuccessEvent() {
        if (status != Status.SUCCESS) {
            when (name) {
                "clickSearch" -> {
                    throw AssertionError("General search event failed. Status = $status")
                }
                "view_item_list" -> {
                    throw AssertionError("Impression event failed. eventCategory: ${data["eventAction"]}. Status = $status")
                }
                "select_content" -> {
                    throw AssertionError("Click event failed. eventCategory: ${data["eventAction"]}. Status = $status")
                }
            }
        }
    }
}
