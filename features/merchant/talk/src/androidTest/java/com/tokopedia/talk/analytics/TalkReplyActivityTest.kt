package com.tokopedia.talk.analytics

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.QUESTION_ID
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SENT_TO_REPLY_TEXT
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.SHOP_ID_VALUE
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_SENT_TO_REPLY_PATH
import com.tokopedia.talk.analytics.util.actionTest
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.sellersettings.template.data.GetAllTemplateResponseWrapper
import com.tokopedia.talk.stub.common.utils.Utils
import com.tokopedia.talk.stub.feature.reply.presentation.activity.TalkReplyActivityStub
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TalkReplyActivityTest : TalkCassavaTestFixture() {

    @get:Rule
    var activityRule = IntentsTestRule(
        TalkReplyActivityStub::class.java,
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
        val intent = TalkReplyActivityStub.createIntent(context, QUESTION_ID, SHOP_ID_VALUE)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickSentToReplyTalk() {
        actionTest {
            typingToReplyTalk()
            clickAction(R.id.replySendButton)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TALK_CLICK_SENT_TO_REPLY_PATH)
        }
    }

    private fun typingToReplyTalk() {
        onView(ViewMatchers.withId(R.id.replyEditText)).perform(replaceText(SENT_TO_REPLY_TEXT), closeSoftKeyboard())
        onView(ViewMatchers.withId(R.id.replyEditText)).check(matches(withText(SENT_TO_REPLY_TEXT)))
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            DiscussionDataByQuestionIDResponseWrapper::class.java,
            Utils.parseFromJson<DiscussionDataByQuestionIDResponseWrapper>("mock_response_discussion_by_question.json")
        )
        graphqlRepositoryStub.createMapResult(
            GetAllTemplateResponseWrapper::class.java,
            Utils.parseFromJson<GetAllTemplateResponseWrapper>("mock_response_chat_templates_all.json")
        )
        graphqlRepositoryStub.createMapResult(
            TalkCreateNewCommentResponseWrapper::class.java,
            Utils.parseFromJson<TalkCreateNewCommentResponseWrapper>("mock_response_talk_create_new_comment.json")
        )
    }
}
