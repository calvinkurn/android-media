package com.tokopedia.talk.analytics.util

import android.content.Context
import com.tokopedia.talk.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class TalkMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_CONTAINS_DISCUSSION_BY_QUESTION, InstrumentationMockHelper.getRawString(context, R.raw.mock_response_discussion_by_question), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_DISCUSSION_INBOX, InstrumentationMockHelper.getRawString(context, R.raw.mock_response_discussion_inbox), FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_DISCUSSION_AGGREGATE_BY_PRODUCT_ID, InstrumentationMockHelper.getRawString(context, R.raw.mock_response_discussion_aggregate_by_product_id), FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_CONTAINS_DISCUSSION_INBOX = "discussionInbox"
        private const val KEY_CONTAINS_DISCUSSION_BY_QUESTION = "discussionDataByQuestionID"
        private const val KEY_CONTAINS_DISCUSSION_AGGREGATE_BY_PRODUCT_ID = "discussionAggregateByProductID"
    }


}