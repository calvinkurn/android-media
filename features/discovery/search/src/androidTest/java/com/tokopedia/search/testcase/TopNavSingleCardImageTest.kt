package com.tokopedia.search.testcase

import android.app.Activity
import android.app.Instrumentation.ActivityResult
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
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.search.*
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.GlobalNavViewHolder
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class TopNavSingleCardImageTest {

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

        setupGraphqlMockResponse(SearchMockModelConfig(com.tokopedia.search.test.R.raw.search_product_response_keyword_ps_3))

        disableOnBoarding(context)

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        intending(isInternal()).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @Test
    fun testTopNavSingleCardImage() {
        performUserJourney()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val productListAdapter = recyclerView.getProductListAdapter()
        val globalNavViewModelPosition = productListAdapter.itemList.getGlobalNavViewModelPosition()

        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<GlobalNavViewHolder>(globalNavViewModelPosition, click()))
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
