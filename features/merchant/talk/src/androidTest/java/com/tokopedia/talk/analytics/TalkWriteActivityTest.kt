package com.tokopedia.talk.analytics

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
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
            return TalkWriteActivity.createIntent(targetContext, PRODUCT_ID_VALUE.toIntOrZero(), false, "")
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(TalkMockResponse())
    }

    @After
    fun tear() {
        clearLogin()
    }

    @Test
    fun validateClickSentNewQuestion() {
        actionTest {
            clickProductVariant()
            clickAction(R.id.talkWriteButton)
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, TALK_CLICK_SENT_NEW_QUESTION_PATH)
            gtmLogDBSource.finishTest()
        }
    }

    private fun clickProductVariant() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.writeCategoryChips)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TalkWriteCategoryChipsWidget.ItemViewHolder>(0, click()))
        onTypingWriteQuestion()
    }

    private fun onTypingWriteQuestion() {
        Espresso.onView(ViewMatchers.withId(R.id.writeQuestionTextArea)).perform(click())
        pauseTestFor(2000L)
        Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.text_area_input)).perform(ViewActions.typeText(SENT_TO_WRITE_QUESTION_TEXT), ViewActions.closeSoftKeyboard())
        pauseTestFor(2000L)
    }
}
