package com.tokopedia.talk.analytics

import android.content.Intent
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.*
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.PRODUCT_ID_VALUE
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SENT_TO_WRITE_QUESTION_TEXT
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_SENT_NEW_QUESTION_PATH
import com.tokopedia.talk.feature.write.presentation.activity.TalkWriteActivity
import com.tokopedia.talk.feature.write.presentation.widget.TalkWriteCategoryChipsWidget
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.UnifyButton
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkWriteActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<TalkWriteActivity> = object : IntentsTestRule<TalkWriteActivity>(TalkWriteActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            fakeLogin()
        }

        override fun getActivityIntent(): Intent {
            return TalkWriteActivity.createIntent(targetContext, PRODUCT_ID_VALUE, false, "")
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    private val idlingResource = object : IdlingResource {
        private var resourceCallback: IdlingResource.ResourceCallback? = null

        override fun getName() = "talk fragment idling resource"

        override fun registerIdleTransitionCallback(
                callback: IdlingResource.ResourceCallback?
        ) {
            resourceCallback = callback
        }

        override fun isIdleNow(): Boolean {
            val talkWriteButton = activityRule.activity.findViewById<UnifyButton>(R.id.talkWriteButton)
            val isIdle = talkWriteButton != null && talkWriteButton.isEnabled
            if (isIdle) {
                resourceCallback?.onTransitionToIdle()
            }
            return isIdle
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(TalkMockResponse())
    }

    @After
    fun tear() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        clearLogin()
    }

    @Test
    fun validateClickSentNewQuestion() {
        actionTest {
            clickProductVariant()
            clickAction(R.id.talkWriteButton)
        } assertTest {
            if (!idlingResource.isIdleNow) {
                performClose(activityRule)
                waitForTrackerSent()
                validate(gtmLogDBSource, targetContext, TALK_CLICK_SENT_NEW_QUESTION_PATH)
                gtmLogDBSource.finishTest()
            }
        }
    }

    private fun clickProductVariant() {
        val viewInteraction = onView(withId(R.id.writeCategoryChips)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TalkWriteCategoryChipsWidget.ItemViewHolder>(0, click()))
        onTypingWriteQuestion()
    }

    private fun onTypingWriteQuestion() {
        onView(withId(R.id.writeQuestionTextArea)).perform(click())
        pauseTestFor(2000L)
        onView(withId(com.tokopedia.unifycomponents.R.id.text_area_input)).perform(typeTextIntoFocusedView(SENT_TO_WRITE_QUESTION_TEXT), closeSoftKeyboard())
        IdlingRegistry.getInstance().register(idlingResource)
        onIdle()
        pauseTestFor(2000L)
    }
}
