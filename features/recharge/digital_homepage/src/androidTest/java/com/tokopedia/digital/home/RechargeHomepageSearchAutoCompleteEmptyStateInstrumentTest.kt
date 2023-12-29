package com.tokopedia.digital.home

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.digital.home.presentation.activity.RechargeHomepageActivity
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class RechargeHomepageSearchAutoCompleteEmptyStateInstrumentTest {

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
            setupGraphqlMockResponse(RechargeHomepageSearchAutoCompleteEmptyStateMockResponse())
        }
    }

    @Test
    fun verify_search_autocomplete_empty_homepage() {
        show_contents_digital_homepage()
        check_search_category()

        Thread.sleep(2000)
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

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
        Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.typeText("asdf"))
        Thread.sleep(1000)
    }

    companion object {
        const val PARAM_PLATFORM_ID_DEFAULT = "31"
        private const val SUBHOME_ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_subhomepage_search_empty.json"
    }
}