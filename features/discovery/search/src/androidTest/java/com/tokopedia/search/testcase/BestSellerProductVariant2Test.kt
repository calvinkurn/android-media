package com.tokopedia.search.testcase

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.search.*
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SuggestionViewHolder
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * created by Dhaba
 */
class BestSellerProductVariant2Test {

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse(SearchMockModelConfig(com.tokopedia.search.test.R.raw.search_product_best_seller_variant_2))

        disableOnBoarding(context)

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        Intents.intending(IntentMatchers.isInternal())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @Test
    fun testBestSellerVariant2() {
        performUserJourney()
    }

    private fun performUserJourney() {
        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        val bestSellerVariant2Position =
            recyclerView.getProductListAdapter().itemList.getVariant2BestSellerProduct()
        Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                bestSellerVariant2Position
            )
        )
        Espresso.onView(ViewMatchers.withId(recyclerViewId)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SuggestionViewHolder>(
                bestSellerVariant2Position,
                ViewActions.click()
            )
        )
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
