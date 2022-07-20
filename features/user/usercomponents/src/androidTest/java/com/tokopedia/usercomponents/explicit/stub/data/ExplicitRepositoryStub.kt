package com.tokopedia.usercomponents.explicit.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usercomponents.common.utils.FileUtils.createResponseFromJson
import com.tokopedia.usercomponents.explicit.domain.model.AnswerDataModel
import com.tokopedia.usercomponents.explicit.domain.model.QuestionDataModel
import com.tokopedia.usercomponents.test.R
import javax.inject.Inject

class ExplicitRepositoryStub @Inject constructor() : GraphqlRepository {

    private var _state: TestState = TestState.HIDE_QUESTION

    fun setState(state: TestState) {
        _state = state
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse =
        when (_state) {
            TestState.HIDE_QUESTION -> createResponseFromJson<QuestionDataModel>(R.raw.response_get_question_failed)
            TestState.SHOW_QUESTION -> createResponseFromJson<QuestionDataModel>(R.raw.response_get_question_success)
            TestState.SUBMIT_QUESTION_SUCCESS -> createResponseFromJson<AnswerDataModel>(R.raw.response_save_question_success)
        }
}