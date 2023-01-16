package com.tokopedia.search.testcase

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.search.*
import com.tokopedia.search.RecyclerViewHasItemIdlingResource
import com.tokopedia.search.SearchMockModelConfig
import com.tokopedia.search.createIntent
import com.tokopedia.search.disableOnBoarding
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchInTokopediaTest {

    @get:Rule
    val activityRule = IntentsTestRule(
        SearchActivity::class.java,
        false,
        false
    )

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null


    @Before
    fun setUp() {
        setupGraphqlMockResponse(
            SearchMockModelConfig(
                com.tokopedia.search.test.R.raw.search_product_last_page_response
            )
        )

        disableOnBoarding(context)

        activityRule.launchActivity(
            createIntent(
                "?q=hairdryer&srp_page_id=383958&navsource=tokocabang&srp_page_titleDilayani+Tokopedia&srp-component_id=02.01.00.00"
            )
        )

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
    fun testSearchInTokopedia() {
        performUserJourney()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val visitableList = recyclerView.getProductListAdapter().itemList
        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(visitableList.size - 1)
        )

        onView(withId(R.id.searchResultGlobalSearchInTokopediaTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.searchResultGlobalSearchInTokopediaButton)).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
