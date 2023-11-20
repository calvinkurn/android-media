package com.tokopedia.digital.home

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.digital.home.presentation.activity.RechargeHomepageActivity
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RechargeHomepageSearchAutocompleteInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule: IntentsTestRule<RechargeHomepageActivity> = object : IntentsTestRule<RechargeHomepageActivity>(RechargeHomepageActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(context, RechargeHomepageActivity::class.java).apply {
                putExtra(RechargeHomepageActivity.PARAM_PLATFORM_ID, PARAM_PLATFORM_ID_DEFAULT)
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(RechargeHomepageSearchAutocompleteMockResponse())
        }
    }

    @Test
    fun verify_search_autocomplete_homepage() {
        show_contents_digital_homepage()
        check_search_category()

        Thread.sleep(2000)
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        click_category_and_operator()

        MatcherAssert.assertThat(
                cassavaTestRule.validate(SUBHOME_ANALYTIC_VALIDATOR_QUERY),
                hasAllSuccess()
        )

    }

    private fun show_contents_digital_homepage() {
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun check_search_category() {
        Espresso.onView(ViewMatchers.withId(R.id.digital_homepage_search_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.typeText("pake"))
        Thread.sleep(1000)
    }

    private fun click_category_and_operator(){
        Thread.sleep(1000)
        val viewInteractionCategory = Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractionCategory.perform(RecyclerViewActions.actionOnItemAtPosition<DigitalHomePageSearchViewHolder>(
                1, ViewActions.click())
        )
        Thread.sleep(1000)
        val viewInteractionOperator = Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractionOperator.perform(RecyclerViewActions.actionOnItemAtPosition<DigitalHomePageSearchDoubleLineViewHolder>(
                3, ViewActions.click())
        )
    }

    companion object {
        const val PARAM_PLATFORM_ID_DEFAULT = "31"
        private const val SUBHOME_ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_subhomepage_search_autocomplete.json"
    }
}