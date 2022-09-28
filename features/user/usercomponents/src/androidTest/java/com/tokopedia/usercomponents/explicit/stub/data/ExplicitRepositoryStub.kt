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

    private var _state: ExplicitRepositoryState = ExplicitRepositoryState.HIDE_QUESTION

    fun setState(state: ExplicitRepositoryState) {
        _state = state
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse =
        when (_state) {
            ExplicitRepositoryState.HIDE_QUESTION -> createResponseFromJson<QuestionDataModel>(R.raw.response_get_question_failed)
            ExplicitRepositoryState.SHOW_QUESTION -> createResponseFromJson<QuestionDataModel>(R.raw.response_get_question_success)
            ExplicitRepositoryState.SUBMIT_QUESTION_SUCCESS -> createResponseFromJson<AnswerDataModel>(R.raw.response_save_question_success)
        }
}