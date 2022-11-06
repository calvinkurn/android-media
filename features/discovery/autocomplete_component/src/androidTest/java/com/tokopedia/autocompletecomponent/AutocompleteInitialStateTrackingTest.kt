package com.tokopedia.autocompletecomponent

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignViewHolder
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateViewHolder
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchViewHolder
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchSeeMoreViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchTitleViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchViewHolder
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewViewHolder
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/autocomplete/initial_state.json"

internal class AutocompleteInitialStateTrackingTest {
    @get:Rule
    val activityRule = IntentsTestRule(AutoCompleteActivityStub::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val recyclerViewId = R.id.recyclerViewInitialState
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null

    @Before
    fun setUp() {

        setupGraphqlMockResponse(AutocompleteMockModelConfig())

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        intending(isInternal()).respondWith(ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)

        recyclerViewIdlingResource = AutocompleteIdlingResource(recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    private fun assertRecyclerViewIsDisplayed() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))
    }

    @Test
    fun testInitialState() {
        assertRecyclerViewIsDisplayed()
        performUserJourney()
        assertCassavaTracker()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))
        onView(withId(recyclerViewId)).perform(ViewActions.closeSoftKeyboard())

        val itemCount = recyclerView?.adapter?.itemCount ?: 0

        recyclerView?.let {
            for (i in 0 until itemCount) {
                it.layoutManager?.smoothScrollToPosition(it, null, i)
                Thread.sleep(1000)
                checkInitialStateItem(it, i)
            }
        }

        activityRule.activity.finish()
    }

    private fun checkInitialStateItem(initialStateRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = initialStateRecyclerView.findViewHolderForAdapterPosition(i)) {
            is CuratedCampaignViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<CuratedCampaignViewHolder>(i, click()))
            }
            is RecentViewViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerViewRecentView, 0)
            }
            is RecentSearchTitleViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<RecentSearchTitleViewHolder>(i,
                        CommonActions.clickChildViewWithId(R.id.actionDeleteButton)))
            }
            is RecentSearchViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerViewRecentSearch, 0)
            }
            is RecentSearchSeeMoreViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<RecentSearchSeeMoreViewHolder>(i,
                        CommonActions.clickChildViewWithId(R.id.autocompleteSeeMoreButton)))
            }
            is PopularSearchTitleViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<PopularSearchTitleViewHolder>(i,
                        CommonActions.clickChildViewWithId(R.id.actionRefreshButton)))
            }
            is PopularSearchViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerViewPopularSearch, 0)
            }
            is DynamicInitialStateTitleViewHolder -> {
                onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItemAtPosition<DynamicInitialStateTitleViewHolder>(i,
                        CommonActions.clickChildViewWithId(R.id.initialStateDynamicButton)))
            }
            is DynamicInitialStateViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerViewDynamicInitialState, 0)
            }
            is InitialStateProductListViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerViewProductList, 0)
            }
        }
    }

    private fun assertCassavaTracker() {
        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
