package com.tokopedia.autocompletecomponent.unify

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.autocompletecomponent.createIntent
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/autocomplete/suggestion_unify.json"
private const val ANALYTIC_VALIDATOR_QUERY_THANOS_ID = "7" // Not known yet

@CassavaTest
class AutoCompleteUnifyTrackingTest {

    private val isFromNetwork = false
    private val queryId
        get() =
            if (isFromNetwork) {
                ANALYTIC_VALIDATOR_QUERY_THANOS_ID
            } else {
                ANALYTIC_VALIDATOR_QUERY_FILE_NAME
            }

    @get:Rule
    val activityRule = IntentsTestRule(
        BaseAutoCompleteActivity::class.java,
        false,
        false
    )

    @get:Rule
    val cassavaTestRule = CassavaTestRule(isFromNetwork)

    @Before
    fun setup() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        activityRule.launchActivity(createIntent())
    }

    @After
    fun teardown() {
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun testImpression() {
        whenSearchForKeyword("samsung")
        thenAssertThatCassavaTestIsValid()
    }

    private fun whenSearchForKeyword(keyword: String) {
        onView(CommonMatcher.firstView(ViewMatchers.withHint("Cari di Tokopedia")))
            .perform(ViewActions.typeText(keyword))
        onView(isRoot()).perform(waitFor(500))
    }

    private fun thenAssertThatCassavaTestIsValid() {
        assertThat(cassavaTestRule.validate(queryId), hasAllSuccess())
    }

    /**
     * Wait for a specific amount of time.
     * @return ViewAction to be used in onView Espresso
     */
    private fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    /**
     * Click on a specific coordinate. Used since we cannot find a specific view in compose
     * @return ViewAction to be used in onView Espresso
     */
    private fun clickXY(x: Int, y: Int): ViewAction {
        return GeneralClickAction(
            Tap.SINGLE,
            { view ->
                val screenPos = IntArray(2)
                view.getLocationOnScreen(screenPos)
                val screenX = (screenPos[0] + x).toFloat()
                val screenY = (screenPos[1] + y).toFloat()
                floatArrayOf(screenX, screenY)
            },
            Press.FINGER
        )
    }
}
