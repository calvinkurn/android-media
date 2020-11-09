package com.tokopedia.play.analytic

import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.play.R
import com.tokopedia.play.analytic.robot.prepare
import com.tokopedia.play.data.PlayLiveMockModelConfig
import com.tokopedia.play.extensions.isFullSolid
import com.tokopedia.play.ui.quickreply.viewholder.QuickReplyViewHolder
import com.tokopedia.play.util.*
import com.tokopedia.play.view.activity.PlayActivity
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 14/09/20.
 */
class CavPlayTrackingLiveTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    @Test
    fun validateTrackingChannelLive() {
        prepare {
            setup(intentsTestRule)
            setMockModel(PlayLiveMockModelConfig())
            launch("1")
            setJsonAbsolutePath("tracker/content/play/play_live_analytic.json")
        } test {
            fakeLogin()
            fakeLaunch()
            performShop()
            performWatchArea()
            performLike()
            performPinnedMessage()
            performSendChatQuickReply()
            performClose()
            Thread.sleep(2000)
            validate()
        }
    }

    /**
     * Journey
     */
    private fun performShop() {
        register(idlResShopInfo)
        Espresso.onView(ViewMatchers.withId(R.id.tv_partner_name)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tv_follow)).perform(ViewActions.click()) // follow
        Espresso.onView(ViewMatchers.withId(R.id.tv_follow)).perform(ViewActions.click()) // unfollow
        unregister(idlResShopInfo)
    }

    private fun performWatchArea() {
        Espresso.onView(ViewMatchers.withId(R.id.v_immersive_box)).perform(ViewActions.click())

        register(idlResImmersive)
        Espresso.onView(ViewMatchers.withId(R.id.v_immersive_box)).perform(ViewActions.click())
        unregister(idlResImmersive)
    }

    private fun performLike() {
        register(idlResLike)
        Espresso.onView(ViewMatchers.withId(R.id.v_like_click_area)).perform(ViewActions.click()) // like
        Espresso.onView(ViewMatchers.withId(R.id.v_like_click_area)).perform(ViewActions.click()) // unlike
        unregister(idlResLike)
    }

    private fun performPinnedMessage() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_pinned_action)).perform(ViewActions.click())
    }

    private fun performSendChatQuickReply() {
        Espresso.onView(ViewMatchers.withId(R.id.et_chat))
                .perform(ViewActions.click())

        Thread.sleep(2000)

        register(idlResKeyboard)
        Espresso.onView(ViewMatchers.withId(R.id.et_chat))
                .perform(ViewActions.typeText("Hello"))

        Espresso.onView(ViewMatchers.withId(R.id.iv_send)).perform(ViewActions.click()) // send chat

        Espresso.onView(ViewMatchers.withId(R.id.rv_quick_reply))
                .perform(RecyclerViewActions.actionOnItemAtPosition<QuickReplyViewHolder>(0, ViewActions.click())) // send quick reply
                .perform(ViewActions.closeSoftKeyboard())
        unregister(idlResKeyboard)
    }

    private fun performClose() {
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.iv_back)).perform(ViewActions.click())
    }

    /**
     * Idling resource
     */
    private val idlResShopInfo by lazy { ComponentIdlingResource(
            object : PlayIdlingResource{
                override fun getName(): String = "clickPartnerInfo"

                override fun idleState(): Boolean {
                    val textView = intentsTestRule.activity.findViewById<AppCompatTextView>(R.id.tv_partner_name)
                    return !TextUtils.isEmpty(textView.text.toString())
                }
            }
    ) }

    private val idlResLike by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource{
                    override fun getName(): String = "clickLike"

                    override fun idleState(): Boolean {
                        val view = intentsTestRule.activity.findViewById<View>(R.id.v_like_click_area)
                        return view.isClickable
                    }
                }
        ) }

    private val idlResKeyboard by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource{
                    override fun getName(): String = "keyboard"

                    override fun idleState(): Boolean {
                        return isKeyboardShown(InstrumentationRegistry.getInstrumentation().targetContext)
                    }
                }
        ) }

    private val idlResImmersive by lazy {
        ComponentIdlingResource(
                object : PlayIdlingResource{
                    override fun getName(): String = "clickImmersive"

                    override fun idleState(): Boolean {
                        val view = intentsTestRule.activity.findViewById<View>(R.id.v_immersive_box)
                        return view.isFullSolid
                    }
                }
        ) }
}