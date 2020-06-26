package com.tokopedia.search

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
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
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.analyticsdebugger.validator.core.*
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
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


private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/search/search_product.json"

internal class SearchTrackingAnalyticValidatorTest {

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val gtmLogDBSource: GtmLogDBSource by lazy { GtmLogDBSource(context) }

    @Before
    fun setUp() {
        editFirebaseRemoteConfig()

        gtmLogDBSource.deleteAll().subscribe()

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        intending(isInternal()).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    private fun editFirebaseRemoteConfig() {
        val firebase = FirebaseRemoteConfigImpl(context)

        firebase.setString(RemoteConfigKey.ENABLE_GLOBAL_NAV_WIDGET, true.toString())
        firebase.setString(RemoteConfigKey.APP_CHANGE_PARAMETER_ROW, false.toString())
        firebase.setString(RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER, true.toString())
        firebase.setString(RemoteConfigKey.ENABLE_TRACKING_VIEW_PORT, true.toString())
        firebase.setString(RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER_REVAMP, true.toString())
    }

    private fun createIntent(): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, SearchActivity::class.java).also {
            it.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?q=samsung")
        }
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    @Test
    fun testTracking() {
        performUserJourney()
        assertTracking()
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

    private fun assertTracking() {
        val testCases = getTestCases()
        val engine = ValidatorEngine(gtmLogDBSource)

        engine.compute(testCases).test {
            it.assertSuccessEvent()
        }
    }

    private fun getTestCases(): List<Validator> {
        val searchProductAnalyticValidator =
                Utils.getJsonDataFromAsset(context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
                        ?: throw AssertionError("Validator Query not found")

        return searchProductAnalyticValidator.toJsonMap().getQueryMap().map { it.toDefaultValidator() }
    }

    private fun Map<String, Any>.getQueryMap(): List<Map<String, Any>> {
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
