package com.tokopedia.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.analyticsdebugger.validator.core.*
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.schedulers.Schedulers
import java.lang.AssertionError

internal class SearchTrackingAnalyticValidatorTest: ProductListFragment.PageLoadCallback {

    class PageLoadCallbackActivityTestRule(
            private val callback: ProductListFragment.PageLoadCallback
    ): ActivityTestRule<SearchActivity>(SearchActivity::class.java, false, false) {

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            activity.setPageLoadCallback(callback)
        }
    }

    @get:Rule
    var activityRule = PageLoadCallbackActivityTestRule(this)

    private var isPageLoaded = false
    private var recyclerView: RecyclerView? = null
    private var visitableList: List<Visitable<*>>? = null
    private val dao: GtmLogDBSource by lazy { GtmLogDBSource(InstrumentationRegistry.getInstrumentation().targetContext) }
    private val engine: ValidatorEngine by lazy { ValidatorEngine(dao) }

    override fun onPageLoadFinished(recyclerView: RecyclerView?, visitableList: List<Visitable<*>>) {
        isPageLoaded = true
        this.recyclerView = recyclerView
        this.visitableList = visitableList
    }

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

    @Test
    fun testTracking() {
        setUpActivity()

        onPageLoaded {
            performUserJourney()
            assertTracking()
        }
    }

    private fun onPageLoaded(onLoaded: () -> Unit) {
        while(!isPageLoaded) { }

        onLoaded()
    }

    private fun performUserJourney() {
        val recyclerViewId = recyclerView?.id ?: throw AssertionError("RecyclerView is null")

        val organicItemPosition = visitableList!!.indexOfFirst { it is ProductItemViewModel && !it.isTopAds }
        val topAdsItemPosition = visitableList!!.indexOfFirst { it is ProductItemViewModel && it.isTopAds }

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
