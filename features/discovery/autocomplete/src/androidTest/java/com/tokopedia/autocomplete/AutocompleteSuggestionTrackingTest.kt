package com.tokopedia.autocomplete

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewHolder
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineWithoutImageViewHolder
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewHolder
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewHolder
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/autocomplete/suggestion.json"

internal class AutocompleteSuggestionTrackingTest {

    @get:Rule
    val activityRule = IntentsTestRule(AutoCompleteActivityStub::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerViewSuggestion
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val gtmLogDBSource = GtmLogDBSource(context)

    private val keyword = "samsung&srp_page_title=local%20search%20test"

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse(AutocompleteMockModelConfig(
                mockModel = com.tokopedia.autocomplete.test.R.raw.suggestion_common_response,
                query = "universe_suggestion"
        ))

        activityRule.launchActivity(createIntent(keyword))

        setupIdlingResource()

        intending(isInternal()).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)

        recyclerViewIdlingResource = AutocompleteIdlingResource(recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @Test
    fun testTracking() {
        performUserJourney()
        assertCassavaTracker()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        performSubmitQueryText()

        val itemCount = recyclerView?.adapter?.itemCount ?: 0

        recyclerView?.let {
            for (i in 0 until itemCount) {
                it.layoutManager?.smoothScrollToPosition(it, null, i)
                checkSuggestionItem(it, i)
            }
        }

        activityRule.activity.finish()
    }

    private fun performSubmitQueryText() {
        onView(withId(R.id.searchTextView)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
    }

    private fun checkSuggestionItem(suggestionRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = suggestionRecyclerView.findViewHolderForAdapterPosition(i)) {
            is SuggestionSingleLineViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<SuggestionSingleLineViewHolder>(i, click()))
            }
            is SuggestionDoubleLineViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<SuggestionDoubleLineViewHolder>(i, click()))
            }
            is SuggestionTopShopWidgetViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.suggestionTopShopCards, 0)
            }
            is SuggestionDoubleLineWithoutImageViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<SuggestionDoubleLineWithoutImageViewHolder>(i, click()))
            }
        }
    }

    private fun assertCassavaTracker() {
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess())
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()

        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
