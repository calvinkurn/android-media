package com.tokopedia.autocompletecomponent.universal

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.autocompletecomponent.AutocompleteIdlingResource
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselViewHolder
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineViewHolder
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class UniversalSearchTrackingTest {
    @get:Rule
    val activityRule = ActivityTestRule(
        UniversalSearchActivityStub::class.java,
        false,
        false,
    )

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val recyclerViewId = R.id.universalSearchRecyclerView
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/autocomplete/universal.json"

    @Before
    fun setUp() {
        setupGraphqlMockResponse(UniversalSearchMockModelConfig())

        activityRule.launchActivity(createUniversalIntent("susu"))

        setupIdlingResource()
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)

        recyclerViewIdlingResource = AutocompleteIdlingResource(recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    private fun createUniversalIntent(queryParams: String = ""): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, UniversalSearchActivityStub::class.java).also {
            it.data = Uri.parse(ApplinkConstInternalDiscovery.UNIVERSAL + "?q=" + queryParams)
        }
    }

    @Test
    fun testUniversalSearch() {
        assertRecyclerViewIsDisplayed()
        performUserJourney()
        assertCassavaTracker()
    }

    private fun assertRecyclerViewIsDisplayed() {
        onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun performUserJourney() {
        val itemCount = recyclerView?.adapter?.itemCount ?: 0

        recyclerView?.let {
            for (i in 0 until itemCount) {
                it.layoutManager?.smoothScrollToPosition(it, null, i)
                Thread.sleep(1000)
                performItemClick(it, i)
            }
        }

        activityRule.activity.finish()
    }

    private fun performItemClick(rv: RecyclerView, i: Int) {
        when (val viewHolder = rv.findViewHolderForAdapterPosition(i)) {
            is CarouselViewHolder -> {
                onView(ViewMatchers.withId(recyclerViewId)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<CarouselViewHolder>(
                        i,
                        CommonActions.clickChildViewWithId(R.id.universalSearchCarouselSeeAll),
                    )
                )

                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, com.tokopedia.carouselproductcard.R.id.carouselProductCardRecyclerView, 1)
            }
            is DoubleLineViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.universalSearchDoubleLineRecyclerView, 1)
            }
        }
    }

    private fun assertCassavaTracker() {
        MatcherAssert.assertThat(
            cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
            hasAllSuccess()
        )
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}
