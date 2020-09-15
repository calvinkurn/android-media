package com.tokopedia.play.analytic

import android.app.Activity
import android.app.Instrumentation
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.play.R
import com.tokopedia.play.data.PlayLiveMockModelConfig
import com.tokopedia.play.ui.quickreply.viewholder.QuickReplyViewHolder
import com.tokopedia.play.util.TextViewIdlingResource
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Test


/**
 * Created by mzennis on 07/09/20.
 * Instrumentation Test for checking Analytics Validator in Play Viewer Module
 *
 * Channel Type: Live
 */
class CavPlayTrackingLiveTest: BaseCavPlayTrackingTest() {

    override fun getFileName(): String = "tracker/content/play/play_live_analytic.json"

    override fun mockModelConfig(): MockModelConfig = PlayLiveMockModelConfig()

    private lateinit var partnerInfo: TextViewIdlingResource

    private fun setupIdlingResource() {

        val tvPartnerInfo = intentsTestRule.activity.findViewById<AppCompatTextView>(R.id.tv_partner_name)
        partnerInfo = TextViewIdlingResource(tvPartnerInfo, "")

        IdlingRegistry.getInstance().register(*arrayOf(partnerInfo))
    }

    @Test
    fun runValidateTracking() {

        // launch play activity with dummy channel id
        intentsTestRule.launchActivity(PlayActivity.createIntent(targetContext, "43215"))

        // idling resource: add artificial delays to the tests
        setupIdlingResource()

        // set login true because we need to test follow - unfollow, quick reply, and send chat
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        // journey
        performShop()
//        performWatchArea()
//        performFollowUnFollowShop()
//        performLikeUnLike()
//        performPinnedMessage()
        performSendChatQuickReply()
        performClose()

        Thread.sleep(2000)

        validateTracker()
    }


    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(partnerInfo)
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