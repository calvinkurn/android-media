package com.tokopedia.talk.analytics

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.PRODUCT_ID_VALUE
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SENT_TO_WRITE_QUESTION_TEXT
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_SENT_NEW_QUESTION_PATH
import com.tokopedia.talk.analytics.util.actionTest
import com.tokopedia.talk.analytics.util.intendingIntent
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.talk.feature.write.data.model.DiscussionSubmitFormResponseWrapper
import com.tokopedia.talk.feature.write.presentation.widget.TalkWriteCategoryChipsWidget
import com.tokopedia.talk.stub.common.utils.Utils
import com.tokopedia.talk.stub.feature.write.presentation.activity.TalkWriteActivityStub
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TalkWriteActivityTest : TalkCassavaTestFixture() {

    @get:Rule
    var activityRule = IntentsTestRule(
        TalkWriteActivityStub::class.java,
        false,
        false
    )

    @Before
    override fun setup() {
        super.setup()
        mockResponses()
        launchActivity()
    }

    override fun launchActivity() {
        val intent = TalkWriteActivityStub.createIntent(
            context = context,
            productId = PRODUCT_ID_VALUE,
            isVariantSelected = false,
            availableVariants = ""
        )
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickSentNewQuestion() {
        actionTest {
            intendingIntent()
            clickProductVariant()
            clickAction(R.id.talkWriteButton)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TALK_CLICK_SENT_NEW_QUESTION_PATH)
        }
    }

    private fun clickProductVariant() {
        val viewInteraction = onView(withId(R.id.writeCategoryChips)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TalkWriteCategoryChipsWidget.ItemViewHolder>(0, click()))
        onTypingWriteQuestion()
    }

    private fun onTypingWriteQuestion() {
        onView(withId(R.id.writeQuestionTextArea)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.text_area_input)).perform(typeTextIntoFocusedView(SENT_TO_WRITE_QUESTION_TEXT), closeSoftKeyboard())
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            DiscussionGetWritingFormResponseWrapper::class.java,
            Utils.parseFromJson<DiscussionGetWritingFormResponseWrapper>("mock_response_discussion_get_writing_form.json")
        )
        graphqlRepositoryStub.createMapResult(
            DiscussionSubmitFormResponseWrapper::class.java,
            Utils.parseFromJson<DiscussionSubmitFormResponseWrapper>("mock_response_discussion_submit_form.json")
        )
    }
}
