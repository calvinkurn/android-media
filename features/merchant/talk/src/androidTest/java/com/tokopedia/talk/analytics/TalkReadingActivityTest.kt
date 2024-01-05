package com.tokopedia.talk.analytics

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_CREATE_NEW_QUESTION_PATH
import com.tokopedia.talk.analytics.util.actionTest
import com.tokopedia.talk.analytics.util.intendingIntent
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.talk.stub.common.utils.Utils
import com.tokopedia.talk.stub.feature.reading.presentation.activity.TalkReadingActivityStub
import org.junit.Rule
import org.junit.Test

class TalkReadingActivityTest : TalkCassavaTestFixture() {
    @get:Rule
    var activityRule = IntentsTestRule(
        TalkReadingActivityStub::class.java,
        false,
        false
    )

    override fun setup() {
        super.setup()
        mockResponses()
        launchActivity()
    }

    override fun launchActivity() {
        val intent = TalkReadingActivityStub.getCallingIntent(context)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickCreateNewQuestion() {
        actionTest {
            intendingIntent()
            clickAction(com.tokopedia.unifycomponents.R.id.fb_circle_icon)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TALK_CLICK_CREATE_NEW_QUESTION_PATH)
        }
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            DiscussionAggregateResponse::class.java,
            Utils.parseFromJson<DiscussionAggregateResponse>("mock_response_discussion_aggregate_by_product_id.json")
        )
        graphqlRepositoryStub.createMapResult(
            DiscussionDataResponseWrapper::class.java,
            Utils.parseFromJson<DiscussionDataResponseWrapper>("mock_response_discussion_data_by_product_id.json")
        )
    }
}
