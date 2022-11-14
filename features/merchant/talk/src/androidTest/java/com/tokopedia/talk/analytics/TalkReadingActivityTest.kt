package com.tokopedia.talk.analytics

import android.app.Application
import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.talk.R
import com.tokopedia.talk.analytics.util.TalkPageRobot.Companion.TALK_CLICK_CREATE_NEW_QUESTION_PATH
import com.tokopedia.talk.analytics.util.actionTest
import com.tokopedia.talk.analytics.util.intendingIntent
import com.tokopedia.talk.analytics.util.waitForTrackerSent
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.talk.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.talk.stub.common.graphql.coroutines.domain.repository.GraphqlRepositoryStub
import com.tokopedia.talk.stub.common.utils.Utils
import com.tokopedia.talk.stub.feature.reading.presentation.activity.TalkReadingActivityStub
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TalkReadingActivityTest {
    @get:Rule
    var activityRule = IntentsTestRule(
        TalkReadingActivityStub::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var graphqlRepositoryStub: GraphqlRepositoryStub

    @Before
    fun setup() {
        getGraphqlRepositoryStub()
        mockResponses()
        val intent = TalkReadingActivityStub.getCallingIntent(context)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickCreateNewQuestion() {
        actionTest {
            intendingIntent()
            clickAction(R.id.fb_circle_icon)
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, TALK_CLICK_CREATE_NEW_QUESTION_PATH)
        }
    }

    private fun getGraphqlRepositoryStub() {
        graphqlRepositoryStub = BaseAppComponentStubInstance.getBaseAppComponentStub(
            context.applicationContext as Application
        ).graphqlRepository() as GraphqlRepositoryStub
        graphqlRepositoryStub.clearMocks()
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
