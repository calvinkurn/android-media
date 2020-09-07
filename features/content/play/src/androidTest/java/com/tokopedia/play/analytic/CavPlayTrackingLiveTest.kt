package com.tokopedia.play.analytic

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.play.R
import com.tokopedia.play.data.PlayLiveMockModelConfig
import com.tokopedia.play.ui.quickreply.viewholder.QuickReplyViewHolder
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 07/09/20.
 * Instrumentation Test for checking Analytics Validator in Play Viewer Module
 *
 * Channel Type: Live
 */
class CavPlayTrackingLiveTest {

    /**
     * This class is an
     * extension of {@link ActivityTestRule}, which initializes Espresso-Intents before each test.
     * This rule also provides functional testing of a single Activity
     * the Activity under test will be launched before each test annotated with @Test
     * and before methods annotated with @Before
     *
     * @param initialTouchMode false because the Activity should not be placed into "touch mode" when started
     * @param launchActivity false because the Activity will be launched manually with intent extras
     **/
    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    // context for the target application being instrumented
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    // database that holds all the analytics every user journey
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    /**
     * json file which holds valid trackers for Live Channel
     * file: libraries/analytics_debugger/src/main/assets/tracker/content/play/play_live_analytic.json
     **/
    private val fileName = "tracker/content/play/play_live_analytic.json"

    @Before
    fun setUp() {
        // delete all data in the database
        gtmLogDbSource.deleteAll().subscribe()

        // setup mock response
        setupGraphqlMockResponse(PlayLiveMockModelConfig())
    }

    @Test
    fun runValidateTracking() {
        // launch play activity with dummy channel id
        intentsTestRule.launchActivity(PlayActivity.createIntent(targetContext, "43215"))

        // idling resource: add artificial delays to the tests
        Thread.sleep(8000)

        // set login true because we need to test follow - unfollow, quick reply, and send chat
        InstrumentationAuthHelper.loginInstrumentationTestUser1(targetContext)

        // journey
        performShop()
        performWatchArea()
        performFollowUnFollowShop()
        performLikeUnLike()
        performPinnedMessage()
        performSendChatQuickReply()
        performClose()

        Thread.sleep(2000)

        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    private fun performShop() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_partner_name)).perform(ViewActions.click()) // shop
        // fake intent activity, the destination activity will not be launched.
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun performWatchArea() {
        Espresso.onView(ViewMatchers.withId(R.id.v_immersive_box)).perform(ViewActions.click())
    }

    private fun performFollowUnFollowShop() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_follow)).perform(ViewActions.click()) // follow
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.tv_follow)).perform(ViewActions.click()) // unfollow
    }

    private fun performLikeUnLike() {
        Espresso.onView(ViewMatchers.withId(R.id.v_like_click_area)).perform(ViewActions.click()) // like
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.v_like_click_area)).perform(ViewActions.click()) // unlike
    }

    private fun performPinnedMessage() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_pinned_action)).perform(ViewActions.click())
    }

    private fun performSendChatQuickReply() {
        Espresso.onView(ViewMatchers.withId(R.id.et_chat)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.et_chat)).perform(ViewActions.typeText("Hello"))
        Espresso.onView(ViewMatchers.withId(R.id.iv_send)).perform(ViewActions.click()) // send chat
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.rv_quick_reply))
                .perform(RecyclerViewActions.actionOnItemAtPosition<QuickReplyViewHolder>(0, ViewActions.click())) // send quick reply
                .perform(closeSoftKeyboard())
        Thread.sleep(2000)
    }

    private fun performClose() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_back)).perform(ViewActions.click())
    }
}